package com.github.jameshnsears.chance.data.repository

import com.fasterxml.jackson.databind.JsonNode
import net.pwall.json.schema.JSONSchema
import timber.log.Timber

class ImportValidation {
    companion object {
        private val schemaSettings = JSONSchema.parse(
            """
                {
                  "type" : "object",
                  "properties" : {
                    "bagDemoBag" : {
                      "type" : "boolean"
                    },
                    "rollDiceTitle" : {
                      "type" : "boolean"
                    },
                    "rollHistory" : {
                      "type" : "boolean"
                    },
                    "rollScore" : {
                      "type" : "boolean"
                    },
                    "rollSequentially" : {
                      "type" : "boolean"
                    },
                    "rollSideNumber" : {
                      "type" : "boolean"
                    },
                    "rollSound" : {
                      "type" : "boolean"
                    },
                    "tabRowChance" : {
                      "type" : "integer",
                      "enum": [0, 1]
                    }
                  },
                  "required": [
                    "bagDemoBag", 
                    "rollDiceTitle", 
                    "rollHistory",
                    "rollScore", 
                    "rollSequentially",
                    "rollSideNumber", 
                    "rollSound",
                    "tabRowChance" 
                    ]
                }                    
                """.trimIndent()
        )

        private val schemaDice = JSONSchema.parse(
            """
                {
                  "type" : "object",
                  "properties" : {
                    "colour" : {
                      "type" : "string"
                    },
                    "epoch": {
                      "type": "string",
                      "pattern": "^[0-9]{13}$"
                    },                    
                    "selected" : {
                      "type" : "boolean"
                    },
                    "title" : {
                      "type" : "string"
                    },
                    "titleStringsId" : {
                      "type" : "integer"
                    }
                  },
                  "required": [
                    "colour", 
                    "epoch", 
                    "selected"
                    ]
                }            
                """.trimIndent()
        )

        private val schemaDiceEpoch = JSONSchema.parse(
            """
                {
                  "type": "object",
                  "properties": {
                    "diceEpoch": {
                      "type": "string",
                      "pattern": "^[0-9]{13}$"
                    }
                  },
                  "required": [
                    "diceEpoch"
                    ]
                }  
                """.trimIndent()
        )

        private val schemaSide = JSONSchema.parse(
            """
                {
                  "type": "object",
                  "properties": {
                    "colour": {
                      "type": "string"
                    },
                    "description": {
                      "type": "string"
                    },
                    "descriptionColour": {
                      "type": "string"
                    },
                    "descriptionStringsId": {
                      "type": "integer"
                    },
                    "imageBase64": {
                      "type": "string"
                    },
                    "imageDrawableId": {
                      "type": "integer"
                    },
                    "number": {
                      "type": "integer"
                    }
                  }
                }
                """.trimIndent()
        )

        fun validate(jsonNode: JsonNode) {
            validateJsonIsNotEmpty(jsonNode)
            validateJsonSectionsExist(jsonNode)

            validateRepositorySettings(jsonNode.get(0))
            validateRepositoryBag(jsonNode.get(1))
            validateRepositoryRoll(jsonNode.get(2))
        }

        private fun validateJsonIsNotEmpty(rootNode: JsonNode) {
            if (rootNode.toString().isEmpty()) throw ImportException()
        }

        private fun validateJsonSectionsExist(rootNode: JsonNode) {
            if (rootNode.size() != 3) throw ImportException()
        }

        fun validateRepositorySettings(jsonSettings: JsonNode) {
            if (!schemaSettings.validate(jsonSettings.toString())) throw ImportException()
        }

        fun validateRepositoryBag(jsonBag: JsonNode) {
            if (jsonBag.toString().isEmpty()) throw ImportException()

            jsonBag.get("dice").forEach { dice ->
                validateDice(dice)
                validateDiceEachSide(dice)
            }
        }

        private fun validateDice(
            dice: JsonNode
        ) {
            val diceString = dice.toString()
            if (!schemaDice.validate(diceString)) {
                logValidationFailure(diceString, schemaDice)
            }
        }

        private fun logValidationFailure(json: String, schema: JSONSchema) {
            val output = schema.validateBasic(json)
            output.errors?.forEach {
                val error = "${it.error} - ${it.instanceLocation}"
                println(error)
                Timber.e(error)
            }
            throw ImportException()
        }

        private fun validateDiceEachSide(
            diceSide: JsonNode,
        ) {
            val sideOfDice = diceSide.get("side")
            if (!setOf(2, 4, 6, 8, 10, 12, 20).contains(sideOfDice.size())) throw ImportException()
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
                            if (!schemaDiceEpoch.validate(diceAndSideString)) {
                                logValidationFailure(diceAndSideString, schemaDiceEpoch)
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
            if (!schemaSide.validate(sideString)) {
                logValidationFailure(sideString, schemaSide)
            }
        }
    }
}