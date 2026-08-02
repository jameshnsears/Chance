package com.github.jameshnsears.chance.data.repo.api

import net.pwall.json.schema.JSONSchema

class RepositoryImportSchemaLegacy {
    companion object {
        val schemaImportRoot = JSONSchema.parse(
            """
            {
              "type": "object",
              "properties": {
                "version": { "type": "string" },
                "settings": { "type": "object" },
                "bag": { "type": "object" },
                "rolls": { "type": "object" },
                "groups": { "type": "object" }
              },
              "required": ["version", "settings", "bag", "rolls"]
            }
            """.trimIndent()
        )

        // Legacy schemas for array-based format (or other versions)
        val schemaSettings = JSONSchema.parse(
            """
            {
              "type": "object",
              "properties": {
                "resizeZoom": { "type": "number" },
                "rollIndexTime": { "type": "boolean" },
                "rollScore": { "type": "boolean" },
                "rollScoreTTS": { "type": "boolean" },
                "diceTitle": { "type": "boolean" },
                "sideNumber": { "type": "boolean" },
                "behaviour": { "type": "boolean" },
                "sideDescription": { "type": "boolean" },
                "sideSVG": { "type": "boolean" },
                "haptics": { "type": "boolean" },
                "shakeToRoll": { "type": "boolean" },
                "rollSound": { "type": "boolean" },
                "shuffle": { "type": "boolean" },
                "groupTitle": { "type": "boolean" }
              },
              "required": [
                "rollIndexTime", "rollScore", "diceTitle", "sideNumber",
                "behaviour", "sideDescription", "sideSVG", "rollSound"
              ]
            }
            """.trimIndent()
        )

        val schemaDice = JSONSchema.parse(
            """
            {
              "type" : "object",
              "properties" : {
                "colour" : { "type" : "string", "maxLength": 8, "minLength": 8 },
                "epoch" : { "type" : "string" },
                "selected" : { "type" : "boolean" },
                "title" : { "type" : "string", "maxLength": 25 },
                "titleStringsId" : { "type" : "integer" },
                "multiplierValue" : { "type" : "integer" },
                "explode" : { "type" : "boolean" },
                "explodeWhen" : { "type" : "string" },
                "explodeValue" : { "type" : "integer" },
                "modifyScore" : { "type" : "boolean" },
                "modifyScoreValue" : { "type" : "integer" },
                "displayIndex" : { "type" : "integer" },
                "uuid": { "type": "string" },
                "side": { "type": "array" }
              },
              "required": ["colour", "epoch", "selected", "side"]
            }
            """.trimIndent()
        )

        val schemaSide = JSONSchema.parse(
            """
            {
              "type": "object",
              "properties": {
                "colour": { "type": "string", "maxLength": 8, "minLength": 8 },
                "description": { "type": "string", "maxLength": 34 },
                "descriptionColour": { "type": "string", "maxLength": 8, "minLength": 8 },
                "descriptionStringsId": { "type": "integer" },
                "imageBase64": { "type": "string" },
                "imageDrawableId": { "type": "integer" },
                "number": { "type": "integer" },
                "numberColour": { "type": "string", "maxLength": 8, "minLength": 8 },
                "uuid": { "type": "string" }
              },
              "required": ["number", "numberColour", "descriptionColour"]
            }
            """.trimIndent()
        )

        val schemaGroup = JSONSchema.parse(
            """
            {
              "type": "object",
              "properties": {
                "group": {
                  "type": "array",
                  "items": {
                    "type": "object",
                    "properties": {
                      "uuidDice": { "type": "array", "items": { "type": "string" } },
                      "modifyScore": { "type": "boolean" },
                      "modifyScoreValue": { "type": "integer" },
                      "displayIndex": { "type": "integer" },
                      "name": { "type": "string" },
                      "notes": { "type": "string" },
                      "uuid": { "type": "string" },
                      "selected": { "type": "boolean" }
                    },
                    "required": ["uuid", "name"]
                  }
                }
              },
              "required": ["group"]
            }
            """.trimIndent()
        )
    }
}
