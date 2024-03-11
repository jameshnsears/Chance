package com.github.jameshnsears.chance.data.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.utility.android.UtilityAndroid
import org.junit.Test

class RepositoryImportValidationUnitTest : UtilityAndroid() {
    @Test(expected = RepositoryImportException::class)
    fun importWithEmptyJson() {
        RepositoryImportValidation.validate(jacksonObjectMapper().readTree(""))
    }

    @Test(expected = RepositoryImportException::class)
    fun importWithEmptyJsonElements() {
        RepositoryImportValidation.validate(
            jacksonObjectMapper().readTree(
                """
                [
                    {},
                    {},
                    {}
                ]
                """.trimIndent()
            )
        )
    }

    @Test
    fun importWithValidSettings() {
        RepositoryImportValidation.validateRepositorySettings(
            jacksonObjectMapper().readTree(
                """          
                {
                  "tabRowChance": 0,
                  "rollSequentially": false,
                  "rollTime": false,
                  "rollScore": false,
                  "rollDiceTitle": false,
                  "rollSideNumber": false,
                  "rollSound": false
                }
                """.trimIndent()
            )
        )
    }

    @Test(expected = RepositoryImportException::class)
    fun importWithInvalidSettings() {
        RepositoryImportValidation.validateRepositorySettings(
            jacksonObjectMapper().readTree(
                """          
                { "tabRowChance": -1 },
                """.trimIndent()
            )
        )
    }

    @Test(expected = RepositoryImportException::class)
    fun importWithInvalidBag() {
        RepositoryImportValidation.validateRepositoryBag(
            jacksonObjectMapper().readTree(
                """          
                {
                    "dice": [
                      {
                        "side": [
                          {
                            "number": 2,
                            "colour": "FF272B22",
                            "imageBase64": "",
                            "imageDrawableId": 0,
                            "description": "",
                            "descriptionStringsId": 2131755044,
                            "descriptionColour": "FF000000"
                          },
                          {
                            "number": 1,
                            "colour": "FF272B22",
                            "imageBase64": "",
                            "imageDrawableId": 0,
                            "description": "",
                            "descriptionStringsId": 2131755057,
                            "descriptionColour": "FF000000"
                          }
                        ],
                        "title": "",
                        "titleStringsId": 2131755045,
                        "colour": "FFE1B530",
                        "selected": false
                      }
                    ]
                }
                """.trimIndent()
            )
        )
    }

    @Test
    fun importWithValidBag() {
        RepositoryImportValidation.validateRepositoryBag(
            jacksonObjectMapper().readTree(
                """          
                {
                    "dice": [
                      {
                        "epoch": "1706951125349",
                        "side": [
                          {
                            "number": 2,
                            "colour": "FF272B22",
                            "imageBase64": "",
                            "imageDrawableId": 0,
                            "description": "",
                            "descriptionStringsId": 2131755044,
                            "descriptionColour": "FF000000"
                          },
                          {
                            "number": 1,
                            "colour": "FF272B22",
                            "imageBase64": "",
                            "imageDrawableId": 0,
                            "description": "",
                            "descriptionStringsId": 2131755057,
                            "descriptionColour": "FF000000"
                          }
                        ],
                        "title": "",
                        "titleStringsId": 2131755045,
                        "colour": "FFE1B530",
                        "selected": false
                      }
                    ]
                }
                """.trimIndent()
            )
        )
    }

    @Test
    fun importWithValidRoll() {
        RepositoryImportValidation.validateRepositoryRoll(
            jacksonObjectMapper().readTree(
                """          
                {
                  "values": {
                    "1706951125381": {
                      "roll": [
                        {
                          "diceEpoch": "1706951125355",
                          "side": {
                            "number": 5,
                            "colour": "FFFFFFFF",
                            "imageBase64": "",
                            "imageDrawableId": 2131230863,
                            "description": "",
                            "descriptionStringsId": 2131755052,
                            "descriptionColour": "FF000000"
                          }
                        },
                        {
                          "diceEpoch": "1706951125355",
                          "side": {
                            "number": 4,
                            "colour": "FFFFFFFF",
                            "imageBase64": "",
                            "imageDrawableId": 2131230864,
                            "description": "",
                            "descriptionStringsId": 2131755053,
                            "descriptionColour": "FF000000"
                          }
                        },
                        {
                          "diceEpoch": "1706951125355",
                          "side": {
                            "number": 3,
                            "colour": "FFFFFFFF",
                            "imageBase64": "",
                            "imageDrawableId": 2131230865,
                            "description": "",
                            "descriptionStringsId": 2131755054,
                            "descriptionColour": "FF000000"
                          }
                        },
                        {
                          "diceEpoch": "1706951125355",
                          "side": {
                            "number": 2,
                            "colour": "FFFFFFFF",
                            "imageBase64": "",
                            "imageDrawableId": 2131230866,
                            "description": "",
                            "descriptionStringsId": 2131755055,
                            "descriptionColour": "FF000000"
                          }
                        },
                        {
                          "diceEpoch": "1706951125355",
                          "side": {
                            "number": 1,
                            "colour": "FFFFFFFF",
                            "imageBase64": "",
                            "imageDrawableId": 2131230867,
                            "description": "",
                            "descriptionStringsId": 2131755056,
                            "descriptionColour": "FF000000"
                          }
                        }
                      ]
                    },
                    "1706951125376": {
                      "roll": [
                        {
                          "diceEpoch": "1706951125355",
                          "side": {
                            "number": 6,
                            "colour": "FFFFFFFF",
                            "imageBase64": "",
                            "imageDrawableId": 2131230862,
                            "description": "",
                            "descriptionStringsId": 2131755051,
                            "descriptionColour": "FF000000"
                          }
                        }
                      ]
                    },
                    "1706951125371": {
                      "roll": [
                        {
                          "diceEpoch": "1706951125349",
                          "side": {
                            "number": 2,
                            "colour": "FF272B22",
                            "imageBase64": "",
                            "imageDrawableId": 0,
                            "description": "",
                            "descriptionStringsId": 2131755044,
                            "descriptionColour": "FF000000"
                          }
                        }
                      ]
                    },
                    "1706951125366": {
                      "roll": [
                        {
                          "diceEpoch": "1706951125355",
                          "side": {
                            "number": 10,
                            "colour": "FFFFFFFF",
                            "imageBase64": "",
                            "imageDrawableId": 2131230858,
                            "description": "",
                            "descriptionStringsId": 2131755047,
                            "descriptionColour": "FF000000"
                          }
                        },
                        {
                          "diceEpoch": "1706951125349",
                          "side": {
                            "number": 1,
                            "colour": "FF272B22",
                            "imageBase64": "",
                            "imageDrawableId": 0,
                            "description": "",
                            "descriptionStringsId": 2131755057,
                            "descriptionColour": "FF000000"
                          }
                        },
                        {
                          "diceEpoch": "1706951125355",
                          "side": {
                            "number": 9,
                            "colour": "FFFFFFFF",
                            "imageBase64": "",
                            "imageDrawableId": 2131230859,
                            "description": "",
                            "descriptionStringsId": 2131755048,
                            "descriptionColour": "FF000000"
                          }
                        },
                        {
                          "diceEpoch": "1706951125349",
                          "side": {
                            "number": 2,
                            "colour": "FF272B22",
                            "imageBase64": "",
                            "imageDrawableId": 0,
                            "description": "",
                            "descriptionStringsId": 2131755044,
                            "descriptionColour": "FF000000"
                          }
                        },
                        {
                          "diceEpoch": "1706951125355",
                          "side": {
                            "number": 8,
                            "colour": "FFFFFFFF",
                            "imageBase64": "",
                            "imageDrawableId": 2131230860,
                            "description": "",
                            "descriptionStringsId": 2131755049,
                            "descriptionColour": "FF000000"
                          }
                        },
                        {
                          "diceEpoch": "1706951125349",
                          "side": {
                            "number": 1,
                            "colour": "FF272B22",
                            "imageBase64": "",
                            "imageDrawableId": 0,
                            "description": "",
                            "descriptionStringsId": 2131755057,
                            "descriptionColour": "FF000000"
                          }
                        },
                        {
                          "diceEpoch": "1706951125355",
                          "side": {
                            "number": 7,
                            "colour": "FFFFFFFF",
                            "imageBase64": "",
                            "imageDrawableId": 2131230861,
                            "description": "",
                            "descriptionStringsId": 2131755050,
                            "descriptionColour": "FF000000"
                          }
                        }
                      ]
                    }
                  }
                }
                """.trimIndent()
            )
        )
    }
}
