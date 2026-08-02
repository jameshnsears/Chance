package com.github.jameshnsears.chance.data.repo.api

import com.fasterxml.jackson.databind.JsonNode
import net.pwall.json.schema.JSONSchema
import timber.log.Timber

data class RepositoryImportData(
    val version: String?,
    val jsonSettings: JsonNode,
    val jsonBag: JsonNode,
    val jsonRolls: JsonNode,
    val jsonGroups: JsonNode?
)

class RepositoryImportValidation(private val currentAppVersion: String) {
    var jsonVersion: String = ""
        private set

    companion object {
        private val VALID_SIDE_COUNTS = setOf(2, 4, 6, 8, 10, 12, 20)
    }

    private data class Schemas(
        val settings: JSONSchema,
        val dice: JSONSchema,
        val side: JSONSchema,
        val group: JSONSchema
    )

    fun validate(jsonNode: JsonNode): RepositoryImportData {
        validateJsonIsNotEmpty(jsonNode)

        return when {
            jsonNode.isArray -> validateLegacy(jsonNode)
            jsonNode.isObject -> validateVersioned(jsonNode)
            else -> throw RepositoryImportException(RepositoryImportStatus.JSON_FILE_MISSING_SECTION)
        }
    }

    private fun validateLegacy(jsonNode: JsonNode): RepositoryImportData {
        if (jsonNode.size() !in 3..4) {
            throw RepositoryImportException(RepositoryImportStatus.JSON_FILE_MISSING_SECTION)
        }

        jsonVersion = "legacy"

        val data = RepositoryImportData(
            version = jsonVersion,
            jsonSettings = jsonNode.get(0),
            jsonBag = jsonNode.get(1),
            jsonRolls = jsonNode.get(2),
            jsonGroups = if (jsonNode.size() > 3) jsonNode.get(3) else null
        )

        val schemas = Schemas(
            RepositoryImportSchemaLegacy.schemaSettings,
            RepositoryImportSchemaLegacy.schemaDice,
            RepositoryImportSchemaLegacy.schemaSide,
            RepositoryImportSchemaLegacy.schemaGroup
        )

        performValidation(data, schemas)
        return data
    }

    private fun validateVersioned(jsonNode: JsonNode): RepositoryImportData {
        RepositoryImportSchemaLegacy.schemaImportRoot.validateNode(
            jsonNode,
            RepositoryImportStatus.JSON_FILE_MISSING_SECTION
        )

        jsonVersion = jsonNode.get("version").asText()

        if (isFutureVersion(jsonVersion, currentAppVersion)) {
            throw RepositoryImportException(RepositoryImportStatus.JSON_FILE_UNKNOWN_VERSION)
        }

        val schemas = if (jsonVersion == "2.5.0") {
            Schemas(
                RepositoryImportSchemaV250.schemaSettings,
                RepositoryImportSchemaV250.schemaDice,
                RepositoryImportSchemaV250.schemaSide,
                RepositoryImportSchemaV250.schemaGroup
            )
        } else {
            Schemas(
                RepositoryImportSchemaLegacy.schemaSettings,
                RepositoryImportSchemaLegacy.schemaDice,
                RepositoryImportSchemaLegacy.schemaSide,
                RepositoryImportSchemaLegacy.schemaGroup
            )
        }

        val data = RepositoryImportData(
            version = jsonVersion,
            jsonSettings = jsonNode.get("settings"),
            jsonBag = jsonNode.get("bag"),
            jsonRolls = jsonNode.get("rolls"),
            jsonGroups = jsonNode.get("groups")
        )

        performValidation(data, schemas)
        return data
    }

    private fun performValidation(data: RepositoryImportData, schemas: Schemas) {
        schemas.settings.validateNode(data.jsonSettings, RepositoryImportStatus.JSON_SCHEMA_SETTINGS)
        validateRepositoryBag(data.jsonBag, schemas.dice, schemas.side)
        validateRepositoryRolls(data.jsonRolls, schemas.side)

        data.jsonGroups?.let {
            if (!it.isNull && !it.isEmpty) {
                schemas.group.validateNode(it, RepositoryImportStatus.JSON_SCHEMA_GROUP)
            }
        }

        val allDiceUuids = diceUuids(data.jsonBag)
        validateRollsReferenceAvailableDice(allDiceUuids, data.jsonRolls)

        data.jsonGroups?.let {
            if (!it.isNull) {
                validateGroupsReferenceAvailableDice(allDiceUuids, it)
            }
        }
    }

    private fun validateRepositoryBag(jsonBag: JsonNode, schemaDice: JSONSchema, schemaSide: JSONSchema) {
        if (jsonBag.isNull || jsonBag.isEmpty || jsonBag.toString() == "{}") {
            throw RepositoryImportException(RepositoryImportStatus.JSON_DICE_MISSING)
        }

        validateDiceTitlesUnique(jsonBag)

        jsonBag.get("dice")?.forEach { dice ->
            schemaDice.validateNode(dice, RepositoryImportStatus.JSON_SCHEMA_DICE)
            validateDiceSides(dice, schemaSide)
        } ?: throw RepositoryImportException(RepositoryImportStatus.JSON_DICE_MISSING)
    }

    private fun validateDiceSides(dice: JsonNode, schemaSide: JSONSchema) {
        val sides = dice.get("side")
        if (sides == null || sides.size() !in VALID_SIDE_COUNTS) {
            throw RepositoryImportException(RepositoryImportStatus.JSON_SIDE_SIZE)
        }
        sides.forEach { side ->
            schemaSide.validateNode(side, RepositoryImportStatus.JSON_SCHEMA_SIDE)
        }
    }

    private fun validateRepositoryRolls(jsonRolls: JsonNode, schemaSide: JSONSchema) {
        jsonRolls.get("values")?.forEach { rollList ->
            rollList.get("roll")?.forEach { roll ->
                roll.get("side")?.let { side ->
                    schemaSide.validateNode(side, RepositoryImportStatus.JSON_SCHEMA_SIDE)
                }
            }
        }
    }

    private fun validateDiceTitlesUnique(jsonBag: JsonNode) {
        val diceTitles = mutableSetOf<String>()
        jsonBag.get("dice")?.forEach { dice ->
            val title = dice.get("title")?.asText()
                ?: throw RepositoryImportException(RepositoryImportStatus.JSON_SCHEMA_DICE)
            if (!diceTitles.add(title)) {
                Timber.e(title)
                throw RepositoryImportException(RepositoryImportStatus.JSON_DICE_TITLE)
            }
        }
    }

    private fun validateRollsReferenceAvailableDice(allDiceUuids: Set<String>, jsonRolls: JsonNode) {
        jsonRolls.get("values")?.forEach { rollList ->
            rollList.get("roll")?.forEach { roll ->
                roll.get("uuidDice")?.asText()?.let { uuid ->
                    if (uuid !in allDiceUuids) {
                        Timber.e(uuid)
                        throw RepositoryImportException(RepositoryImportStatus.JSON_DICE_UUID)
                    }
                }
            }
        }
    }

    private fun validateGroupsReferenceAvailableDice(allDiceUuids: Set<String>, jsonGroups: JsonNode) {
        jsonGroups.get("group")?.forEach { group ->
            group.get("uuidDice")?.forEach { uuidDice ->
                val uuid = uuidDice.asText()
                if (uuid !in allDiceUuids) {
                    Timber.e(uuid)
                    throw RepositoryImportException(RepositoryImportStatus.JSON_DICE_UUID)
                }
            }
        }
    }

    private fun diceUuids(jsonBag: JsonNode): Set<String> {
        return jsonBag.get("dice")?.mapNotNull { it.get("uuid")?.asText() }?.toSet() ?: emptySet()
    }

    private fun isFutureVersion(version: String, currentAppVersion: String): Boolean {
        val v1 = version.split(".").map { it.toIntOrNull() ?: 0 }
        val v2 = currentAppVersion.split(".").map { it.toIntOrNull() ?: 0 }

        for (i in 0 until maxOf(v1.size, v2.size)) {
            val part1 = v1.getOrElse(i) { 0 }
            val part2 = v2.getOrElse(i) { 0 }
            if (part1 > part2) return true
            if (part1 < part2) return false
        }
        return false
    }

    private fun validateJsonIsNotEmpty(rootNode: JsonNode) {
        if (rootNode.toString().isEmpty()) {
            throw RepositoryImportException(RepositoryImportStatus.JSON_FILE_EMPTY)
        }
    }

    private fun JSONSchema.validateNode(node: JsonNode, status: RepositoryImportStatus) {
        val json = node.toString()
        if (!validate(json)) {
            val output = validateBasic(json)
            Timber.e("$status")
            output.errors?.forEach {
                Timber.e("${it.error} - ${it.instanceLocation}")
            }
            if (BuildConfig.DEBUG) {
                output.errors?.forEach {
                    println("${it.error} - ${it.instanceLocation}")
                }
            }
            throw RepositoryImportException(status)
        }
    }
}
