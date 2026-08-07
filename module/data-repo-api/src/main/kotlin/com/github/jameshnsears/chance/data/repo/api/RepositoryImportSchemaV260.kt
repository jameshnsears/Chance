package com.github.jameshnsears.chance.data.repo.api

import net.pwall.json.schema.JSONSchema

class RepositoryImportSchemaV260 : RepositoryImportSchemaV25() {
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
    "groupTitle": { "type": "boolean" },
    "orientation": { "type": "boolean" }
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
    "groupTitle",
    "orientation"
  ],
  "additionalProperties": false
}
            """.trimIndent()
        )

        val schemaDice = RepositoryImportSchemaV25.schemaDice
        val schemaSide = RepositoryImportSchemaV25.schemaSide
        val schemaGroup = RepositoryImportSchemaV25.schemaGroup
    }
}
