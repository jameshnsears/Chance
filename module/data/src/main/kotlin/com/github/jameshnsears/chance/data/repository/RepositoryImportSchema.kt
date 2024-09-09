package com.github.jameshnsears.chance.data.repository

import net.pwall.json.schema.JSONSchema

class RepositoryImportSchema {
    companion object {
        val schemaSettings = JSONSchema.parse(
            """
            {
              "type": "object",
              "properties": {
                "resize": {
                  "type": "number"
                },
                "rollIndexTime": {
                  "type": "boolean"
                },
                "rollScore": {
                  "type": "boolean"
                },
                "diceTitle": {
                  "type": "boolean"
                },
                "sideNumber": {
                  "type": "boolean"
                },
                "behaviour": {
                  "type": "boolean"
                },
                "sideDescription": {
                  "type": "boolean"
                },
                "sideSVG": {
                  "type": "boolean"
                },
                "rollSound": {
                  "type": "boolean"
                }
              },
              "required": [
                "resize",
                "rollIndexTime",
                "rollScore",
                "diceTitle",
                "sideNumber",
                "behaviour",
                "sideDescription",
                "sideSVG",
                "rollSound"
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
                      "type" : "string",
                      "maxLength": 8,
                      "minLength": 8
                    },
                    "epoch": {
                      "type": "string",
                      "pattern": "^[0-9]{13}$",
                      "maxLength": 13
                    },                    
                    "selected" : {
                      "type" : "boolean"
                    },
                    "title" : {
                      "type" : "string",
                      "maxLength": 25
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

        val schemaSide = JSONSchema.parse(
            """
                {
                  "type": "object",
                  "properties": {
                    "colour": {
                      "type": "string",
                      "maxLength": 8,
                      "minLength": 8
                    },
                    "description": {
                      "type": "string",
                      "maxLength": 25
                    },
                    "descriptionColour": {
                      "type": "string",
                      "maxLength": 8,
                      "minLength": 8
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
                    },
                    "numberColour": {
                      "type": "string",
                      "maxLength": 8,
                      "minLength": 8
                    }
                  },
                  "required": [
                    "number", 
                    "numberColour", 
                    "descriptionColour"
                    ]
                }  
                """.trimIndent()
        )
    }
}