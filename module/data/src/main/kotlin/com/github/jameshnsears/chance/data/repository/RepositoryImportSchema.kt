package com.github.jameshnsears.chance.data.repository

import net.pwall.json.schema.JSONSchema

class RepositoryImportSchema {
    companion object {
        val schemaSettings = JSONSchema.parse(
            """
                {
                  "type" : "object",
                  "properties" : {
                    "rollDiceTitle" : {
                      "type" : "boolean"
                    },
                    "rollTime" : {
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
                    "rollDiceTitle", 
                    "rollTime",
                    "rollScore", 
                    "rollSequentially",
                    "rollSideNumber", 
                    "rollSound",
                    "tabRowChance" 
                    ]
                }                    
                """.trimIndent()
        )

        val schemaDice = JSONSchema.parse(
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
                    },
                    "rollMultiplier" : {
                      "type" : "boolean"
                    },
                    "rollMultiplierValue" : {
                      "type" : "integer"
                    },
                    "rollExplode" : {
                      "type" : "boolean"
                    },
                    "rollExplodeWhen" : {
                      "type" : "string"
                    },
                    "rollExplodeValue" : {
                      "type" : "integer"
                    },
                    "rollModifyScore" : {
                      "type" : "boolean"
                    },
                    "rollModifyScoreValue" : {
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

        val schemaDiceEpoch = JSONSchema.parse(
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

        val schemaSide = JSONSchema.parse(
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
    }
}