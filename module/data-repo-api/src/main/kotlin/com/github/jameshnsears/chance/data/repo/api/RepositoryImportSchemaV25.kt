package com.github.jameshnsears.chance.data.repo.api

import net.pwall.json.schema.JSONSchema

open class RepositoryImportSchemaV25 {
    companion object {
        val schemaSettings = JSONSchema.parse(
            """
{
  "type": "object",
  "properties": {
    "rollIndexTime": { "type": "boolean" },
    "rollScore": { "type": "boolean" },
    "diceTitle": { "type": "boolean" },
    "sideNumber": { "type": "boolean" },
    "behaviour": { "type": "boolean" },
    "sideDescription": { "type": "boolean" },
    "sideSVG": { "type": "boolean" },
    "rollSound": { "type": "boolean" },
    "shuffle": { "type": "boolean" },
    "haptics": { "type": "boolean" },
    "rollScoreTTS": { "type": "boolean" },
    "shakeToRoll": { "type": "boolean" },
    "resizeZoom": { "type": "number" },
    "groupTitle": { "type": "boolean" }
  },
  "required": [
    "rollIndexTime",
    "rollScore",
    "diceTitle",
    "sideNumber",
    "behaviour",
    "sideDescription",
    "sideSVG",
    "rollSound",
    "shuffle",
    "haptics",
    "rollScoreTTS",
    "shakeToRoll",
    "resizeZoom",
    "groupTitle"
  ],
  "additionalProperties": false
}
            """.trimIndent()
        )

        val schemaDice = JSONSchema.parse(
            """
{
  "type": "object",
  "properties": {
    "epoch": { "type": "string", "pattern": "^[0-9]+$" },
    "uuid": { "type": "string", "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$" },
    "side": { "type": "array" },
    "title": { "type": "string", "maxLength": 25 },
    "colour": { "type": "string", "maxLength": 8, "minLength": 8, "pattern": "^[0-9A-Fa-f]{8}$" },
    "selected": { "type": "boolean" },
    "multiplierValue": { "type": "integer" },
    "explode": { "type": "boolean" },
    "explodeWhen": { "type": "string" },
    "explodeValue": { "type": "integer" },
    "modifyScore": { "type": "boolean" },
    "modifyScoreValue": { "type": "integer" },
    "displayIndex": { "type": "integer" }
  },
  "required": [
    "epoch",
    "uuid",
    "side",
    "title",
    "colour",
    "selected",
    "multiplierValue",
    "explode",
    "explodeWhen",
    "explodeValue",
    "modifyScore",
    "modifyScoreValue",
    "displayIndex"
  ],
  "additionalProperties": false
}
            """.trimIndent()
        )

        val schemaSide = JSONSchema.parse(
            """
{
  "${'$'}defs": {
    "uuid": { "type": "string", "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$" },
    "colour": { "type": "string", "minLength": 8, "maxLength": 8, "pattern": "^[0-9A-Fa-f]{8}$" }
  },
  "type": "object",
  "properties": {
    "uuid": { "${'$'}ref": "#/${'$'}defs/uuid" },
    "number": { "type": "integer" },
    "numberColour": { "${'$'}ref": "#/${'$'}defs/colour" },
    "imageDrawableId": { "type": "integer" },
    "imageBase64": { "type": "string" },
    "description": { "type": "string", "maxLength": 34 },
    "descriptionColour": { "${'$'}ref": "#/${'$'}defs/colour" },
    "imageBase64Uuid": { "${'$'}ref": "#/${'$'}defs/uuid" }
  },
  "required": [
    "uuid"
  ],
  "additionalProperties": false
}
            """.trimIndent()
        )

        val schemaGroup = JSONSchema.parse(
            """
{
  "${'$'}defs": {
    "uuid": { "type": "string", "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$" }
  },
  "type": "object",
  "properties": {
    "group": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "uuid": { "${'$'}ref": "#/${'$'}defs/uuid" },
          "name": { "type": "string" },
          "uuidDice": {
            "type": "array",
            "items": { "${'$'}ref": "#/${'$'}defs/uuid" }
          },
          "notes": { "type": "string" },
          "displayIndex": { "type": "integer" },
          "selected": { "type": "boolean" }
        },
        "required": [
          "uuid",
          "name",
          "uuidDice",
          "displayIndex",
          "selected"
        ],
        "additionalProperties": false
      }
    }
  },
  "required": [
    "group"
  ],
  "additionalProperties": false
}
            """.trimIndent()
        )
    }
}
