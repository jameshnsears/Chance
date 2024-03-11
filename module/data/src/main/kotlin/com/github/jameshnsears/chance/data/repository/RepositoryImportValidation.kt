package com.github.jameshnsears.chance.data.repository

import com.fasterxml.jackson.databind.JsonNode
import net.pwall.json.schema.JSONSchema
import timber.log.Timber

class RepositoryImportValidation {
    companion object {
        fun validate(jsonNode: JsonNode) {
            validateJsonIsNotEmpty(jsonNode)
            validateJsonSectionsExist(jsonNode)

            validateRepositorySettings(jsonNode.get(0))
            validateRepositoryBag(jsonNode.get(1))
            validateRepositoryRoll(jsonNode.get(2))
        }

        private fun validateJsonIsNotEmpty(rootNode: JsonNode) {
            if (rootNode.toString().isEmpty()) throw RepositoryImportException()
        }

        private fun validateJsonSectionsExist(rootNode: JsonNode) {
            if (rootNode.size() != 3) throw RepositoryImportException()
        }

        fun validateRepositorySettings(jsonSettings: JsonNode) {
            if (!RepositoryImportSchema.schemaSettings.validate(jsonSettings.toString())) throw RepositoryImportException()
        }

        fun validateRepositoryBag(jsonBag: JsonNode) {
            if (jsonBag.toString().isEmpty()) throw RepositoryImportException()

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
                logValidationFailure(diceString, RepositoryImportSchema.schemaDice)
            }
        }

        private fun logValidationFailure(json: String, schema: JSONSchema) {
            val output = schema.validateBasic(json)
            output.errors?.forEach {
                val error = "${it.error} - ${it.instanceLocation}"
                println(error)
                Timber.e(error)
            }
            throw RepositoryImportException()
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
            ) throw RepositoryImportException()
            sideOfDice.forEach { side ->
                validateSide(side)
            }
        }

        fun validateRepositoryRoll(jsonRoll: JsonNode) {
            jsonRoll.forEach { rollHistory ->
                rollHistory.forEach { rollSequence ->
                    rollSequence.forEach { roll ->
                        roll.forEach { diceAndSide ->
                            val diceAndSideString = diceAndSide.toString()
                            if (!RepositoryImportSchema.schemaDiceEpoch.validate(diceAndSideString)) {
                                logValidationFailure(
                                    diceAndSideString,
                                    RepositoryImportSchema.schemaDiceEpoch
                                )
                            }
                            validateSide(diceAndSide.get("side"))
                        }
                    }
                }
            }
        }

        private fun validateSide(
            side: JsonNode,
        ) {
            val sideString = side.toString()
            if (!RepositoryImportSchema.schemaSide.validate(sideString)) {
                logValidationFailure(sideString, RepositoryImportSchema.schemaSide)
            }
        }
    }
}
