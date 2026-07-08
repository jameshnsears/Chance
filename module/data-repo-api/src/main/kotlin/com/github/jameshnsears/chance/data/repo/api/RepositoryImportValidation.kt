package com.github.jameshnsears.chance.data.repo.api

import com.fasterxml.jackson.databind.JsonNode
import net.pwall.json.schema.JSONSchema
import timber.log.Timber

class RepositoryImportValidation {
    companion object {
        fun validate(jsonNode: JsonNode) {
            validateJsonIsNotEmpty(jsonNode)
            validateJsonSectionsExist(jsonNode)

            val jsonSettings = jsonNode.get(0)
            val jsonBag = jsonNode.get(1)
            val jsonRolls = jsonNode.get(2)
            val jsonGroups = jsonNode.get(3)

            validateRepositorySettings(jsonSettings)
            validateRepositoryBag(jsonBag)
            validateRepositoryRolls(jsonRolls)
            validateRepositoryGroups(jsonGroups)

            validateDiceIntegrity(jsonBag)
            validateRollsReferenceAvailableDice(jsonBag, jsonRolls)

            if (jsonGroups != null)
                validateGroupsReferenceAvailableDice(jsonBag, jsonGroups)
        }

        private fun validateJsonIsNotEmpty(rootNode: JsonNode) {
            if (rootNode.toString().isEmpty())
                throw RepositoryImportException(RepositoryImportStatus.ERROR_IMPORT_EMPTY)
        }

        private fun validateJsonSectionsExist(rootNode: JsonNode) {
            if (rootNode.size() !in 2..4)
                throw RepositoryImportException(RepositoryImportStatus.ERROR_SECTION_MISSING)
        }

        private fun validateRepositorySettings(jsonSettings: JsonNode) {
            val settingsString = jsonSettings.toString()

            if (!RepositoryImportSchema.schemaSettings.validate(settingsString))
                logSchemaValidationFailure(
                    settingsString,
                    RepositoryImportSchema.schemaSettings,
                    RepositoryImportStatus.ERROR_SCHEMA_SETTINGS
                )
        }

        private fun validateRepositoryBag(jsonBag: JsonNode) {
            if (jsonBag.toString() == "{}")
                throw RepositoryImportException(RepositoryImportStatus.ERROR_DICE_MISSING)

            validateDiceTitle(jsonBag)

            jsonBag.get("dice").forEach { dice ->
                validateDice(dice)
                validateDiceEachSide(dice)
            }
        }

        private fun validateDice(
            dice: JsonNode
        ) {
            val diceString = dice.toString()
            if (!RepositoryImportSchema.schemaDice.validate(diceString)) {
                logSchemaValidationFailure(
                    diceString,
                    RepositoryImportSchema.schemaDice,
                    RepositoryImportStatus.ERROR_SCHEMA_DICE
                )
            }
        }

        private fun logSchemaValidationFailure(
            json: String,
            schema: JSONSchema,
            reason: RepositoryImportStatus
        ) {
            val output = schema.validateBasic(json)

            Timber.e("$reason")

            output.errors?.forEach {
                val errorMessage = "${it.error} - ${it.instanceLocation}"
                Timber.e(errorMessage)

                if (BuildConfig.DEBUG) {
                    println(errorMessage)
                }
            }

            throw RepositoryImportException(reason)
        }

        private fun validateDiceEachSide(
            diceSide: JsonNode,
        ) {
            val sideOfDice = diceSide.get("side")
            if (!setOf(
                    2,
                    4,
                    6,
                    8,
                    10,
                    12,
                    20
                ).contains(sideOfDice.size())
            ) throw RepositoryImportException(RepositoryImportStatus.ERROR_SIDE_SIZE)
            sideOfDice.forEach { side ->
                validateSide(side)
            }
        }

        private fun validateRepositoryRolls(jsonRolls: JsonNode) {
            jsonRolls.forEach { rollHistory ->
                rollHistory.forEach { rollSequence ->
                    rollSequence.forEach { roll ->
                        roll.forEach { diceAndSide ->
                            validateSide(diceAndSide.get("side"))
                        }
                    }
                }
            }
        }

        private fun validateRepositoryGroups(jsonGroups: JsonNode?) {
            if (jsonGroups != null && !jsonGroups.isEmpty) {
                validateGroup(jsonGroups)
            }
        }

        private fun validateSide(
            side: JsonNode,
        ) {
            val sideString = side.toString()
            if (!RepositoryImportSchema.schemaSide.validate(sideString)) {
                logSchemaValidationFailure(
                    sideString,
                    RepositoryImportSchema.schemaSide,
                    RepositoryImportStatus.ERROR_SCHEMA_SIDE
                )
            }
        }

        private fun validateGroup(
            group: JsonNode,
        ) {
            val groupString = group.toString()
            if (!RepositoryImportSchema.schemaGroup.validate(groupString)) {
                logSchemaValidationFailure(
                    groupString,
                    RepositoryImportSchema.schemaGroup,
                    RepositoryImportStatus.ERROR_SCHEMA_GROUP
                )
            }
        }

        private fun validateDiceTitle(jsonBag: JsonNode) {
            val diceTitles = ArrayList<String>()
            jsonBag.get("dice").forEach { dice ->

                if (dice.get("title") == null) {
                    throw RepositoryImportException(RepositoryImportStatus.ERROR_SCHEMA_DICE)
                }

                val diceTitle = dice.get("title").asText()

                if (diceTitles.contains(diceTitle)) {
                    Timber.e(diceTitle)
                    throw RepositoryImportException(RepositoryImportStatus.ERROR_DICE_TITLE)
                }

                diceTitles.add(diceTitle)
            }
        }

        private fun validateRollsReferenceAvailableDice(
            jsonBag: JsonNode,
            jsonRolls: JsonNode
        ) {
            val allDiceUuids = diceUuids(jsonBag)

            jsonRolls.forEach { rollHistory ->
                rollHistory.forEach { rollSequence ->
                    rollSequence.forEach { roll ->
                        roll.forEach { diceAndSide ->
                            val uuidDice = diceAndSide.get("uuidDice")
                            if (uuidDice != null) {
                                val sideDiceUuid = uuidDice.asText()

                                if (!allDiceUuids.contains(sideDiceUuid)) {
                                    Timber.e(sideDiceUuid)
                                    throw RepositoryImportException(RepositoryImportStatus.ERROR_DICE_UNKNOWN)
                                }
                            }
                        }
                    }
                }
            }
        }

        private fun validateGroupsReferenceAvailableDice(jsonBag: JsonNode, jsonGroups: JsonNode) {
            val allDiceUuids = diceUuids(jsonBag)

            jsonGroups.get("group")?.forEach { group ->
                group.get("uuidDice")?.forEach { uuidDice ->
                    if (!allDiceUuids.contains(uuidDice.asText())) {
                        Timber.e(uuidDice.asText())
                        throw RepositoryImportException(RepositoryImportStatus.ERROR_DICE_UNKNOWN)
                    }
                }
            }
        }

        private fun validateDiceIntegrity(jsonBag: JsonNode) = diceUuids(jsonBag)

        private fun diceUuids(jsonNode: JsonNode): List<String> {
            return jsonNode.get("dice")?.mapNotNull { dice ->
                dice.get("uuid")?.asText()
            } ?: emptyList()
        }
    }
}
