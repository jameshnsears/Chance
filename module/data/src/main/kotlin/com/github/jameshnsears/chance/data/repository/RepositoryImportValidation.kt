package com.github.jameshnsears.chance.data.repository

import com.fasterxml.jackson.databind.JsonNode
import net.pwall.json.schema.JSONSchema
import timber.log.Timber

class RepositoryImportValidation {
    companion object {
        fun validate(jsonNode: JsonNode) {
            validateJsonIsNotEmpty(jsonNode)
            validateJsonSectionsExist(jsonNode)

            validateDiceBagNotEmpty(jsonNode.get(1))
            validateDiceTitle(jsonNode.get(1))
            validateRollsReferenceAvailableDice(jsonNode.get(1), jsonNode.get(2))

            validateRepositorySettings(jsonNode.get(0))
            validateRepositoryBag(jsonNode.get(1))
            validateRepositoryRoll(jsonNode.get(2))

            validateEpochData(jsonNode)
        }

        private fun validateJsonIsNotEmpty(rootNode: JsonNode) {
            if (rootNode.toString().isEmpty())
                throw RepositoryImportException(RepositoryImportStatus.ERROR_IMPORT_EMPTY)
        }

        private fun validateJsonSectionsExist(rootNode: JsonNode) {
            if (rootNode.size() != 3)
                throw RepositoryImportException(RepositoryImportStatus.ERROR_SECTION_MISSING)
        }

        private fun validateRepositorySettings(jsonSettings: JsonNode) {
            val settingsString = jsonSettings.toString()

            if (!RepositoryImportSchema.schemaSettings.validate(settingsString))
                logSchemaValidationFailure(
                    settingsString,
                    RepositoryImportSchema.schemaDice,
                    RepositoryImportStatus.ERROR_SCHEMA_SETTINGS
                )
        }

        private fun validateRepositoryBag(jsonBag: JsonNode) {
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

            println("$reason")
            Timber.e("$reason")

            output.errors?.forEach {
                val err = "${it.error} - ${it.instanceLocation}"
                println(err)
                Timber.e(err)
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

        private fun validateRepositoryRoll(jsonRoll: JsonNode) {
            jsonRoll.forEach { rollHistory ->
                rollHistory.forEach { rollSequence ->
                    rollSequence.forEach { roll ->
                        roll.forEach { diceAndSide ->
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
                logSchemaValidationFailure(
                    sideString,
                    RepositoryImportSchema.schemaSide,
                    RepositoryImportStatus.ERROR_SCHEMA_SIDE
                )
            }
        }

        private fun validateDiceBagNotEmpty(jsonBag: JsonNode) {
            if (jsonBag.toString() == "{}")
                throw RepositoryImportException(RepositoryImportStatus.ERROR_DICE_MISSING)
        }

        private fun validateDiceTitle(jsonBag: JsonNode) {
            val diceTitles = ArrayList<String>()
            jsonBag.get("dice").forEach { dice ->
                val diceTitle = dice.get("title").toString()

                if (diceTitles.contains(diceTitle)) {
                    Timber.e(diceTitle)
                    throw RepositoryImportException(RepositoryImportStatus.ERROR_DICE_TITLE)
                }

                diceTitles.add(diceTitle)
            }
        }

        private fun validateRollsReferenceAvailableDice(
            jsonBag: JsonNode,
            jsonRoll: JsonNode
        ) {
            val allDiceEpochs = diceEpochs(jsonBag)

            jsonRoll.forEach { rollHistory ->
                rollHistory.forEach { rollSequence ->
                    rollSequence.forEach { roll ->
                        roll.forEach { diceAndSide ->
                            val sideDiceEpoch = diceAndSide.get("diceEpoch").toString()

                            if (!allDiceEpochs.contains(sideDiceEpoch)) {
                                Timber.e(sideDiceEpoch)
                                throw RepositoryImportException(RepositoryImportStatus.ERROR_DICE_UNKNOWN)
                            }
                        }
                    }
                }
            }
        }

        private fun validateEpochData(rootNode: JsonNode) {
            //  roll side diceEpoch must be known dice epoch
            val diceEpochs = diceEpochs(rootNode.get(1))

            rootNode.get(2).forEach { rollHistory ->
                rollHistory.forEach { rollSequence ->
                    rollSequence.forEach { roll ->
                        roll.forEach { diceAndSide ->
                            val sideDiceEpoch = diceAndSide.get("diceEpoch").toString()
                            if (!diceEpochs.contains(sideDiceEpoch)) {
                                Timber.e(sideDiceEpoch)
                                throw RepositoryImportException(RepositoryImportStatus.ERROR_DICE_UNKNOWN)
                            }
                        }
                    }
                }
            }
        }

        private fun diceEpochs(jsonNode: JsonNode): ArrayList<String> {
            val diceEpochs = ArrayList<String>()
            jsonNode.get("dice").forEach { dice ->
                diceEpochs.add(dice.get("epoch").toString())
            }
            return diceEpochs
        }
    }
}
