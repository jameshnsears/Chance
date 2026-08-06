package com.github.jameshnsears.chance.data.repo.api

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.UUID

class RepositoryImportValidationUnitTest {
    private val mapper = jacksonObjectMapper()

    private fun createValidSettingsV250(): ObjectNode {
        return mapper.createObjectNode().apply {
            put("rollIndexTime", true)
            put("rollScore", true)
            put("diceTitle", true)
            put("sideNumber", true)
            put("behaviour", true)
            put("sideDescription", true)
            put("sideSVG", true)
            put("rollSound", true)
            put("shuffle", true)
            put("haptics", true)
            put("rollScoreTTS", true)
            put("shakeToRoll", true)
            put("resizeZoom", 1.0)
            put("groupTitle", true)
        }
    }

    private fun createValidSettingsV260(): ObjectNode {
        return mapper.createObjectNode().apply {
            put("rollIndexTime", true)
            put("rollScore", true)
            put("diceTitle", true)
            put("sideNumber", true)
            put("behaviour", true)
            put("sideDescription", true)
            put("sideSVG", true)
            put("rollSound", true)
            put("shuffle", true)
            put("haptics", true)
            put("rollScoreTTS", true)
            put("shakeToRoll", true)
            put("resizeZoom", 1.0)
            put("groupTitle", true)
            put("history", true)
        }
    }

    private fun createValidSideV250(): ObjectNode {
        return mapper.createObjectNode().apply {
            put("uuid", UUID.randomUUID().toString())
            put("number", 1)
            put("numberColour", "FF000000")
            put("imageDrawableId", 0)
            put("imageBase64", "")
            put("description", "")
            put("descriptionColour", "FF000000")
        }
    }

    private fun createValidDiceV250(title: String = "Dice"): ObjectNode {
        val dice = mapper.createObjectNode().apply {
            put("epoch", "123456789")
            put("uuid", UUID.randomUUID().toString())
            put("title", title)
            put("colour", "FFFFFFFF")
            put("selected", true)
            put("multiplierValue", 1)
            put("explode", false)
            put("explodeWhen", "")
            put("explodeValue", 0)
            put("modifyScore", false)
            put("modifyScoreValue", 0)
            put("displayIndex", 0)
        }
        val sides = dice.putArray("side")
        repeat(6) { sides.add(createValidSideV250()) }
        return dice
    }

    private fun createValidRootV250(): ObjectNode {
        val root = mapper.createObjectNode()
        root.put("version", "2.5.0")
        root.set<ObjectNode>("settings", createValidSettingsV250())
        val bag = root.putObject("bag")
        bag.putArray("dice").add(createValidDiceV250())
        root.putObject("rolls").putArray("values")
        root.putObject("groups").putArray("group")
        return root
    }

    private fun createValidRootV260(): ObjectNode {
        val root = mapper.createObjectNode()
        root.put("version", "2.6.0")
        root.set<ObjectNode>("settings", createValidSettingsV260())
        val bag = root.putObject("bag")
        bag.putArray("dice").add(createValidDiceV250())
        root.putObject("rolls").putArray("values")
        root.putObject("groups").putArray("group")
        return root
    }

    private fun createValidLegacyRoot(): ArrayNode {
        val root = mapper.createArrayNode()

        val settings = mapper.createObjectNode().apply {
            put("rollIndexTime", true)
            put("rollScore", true)
            put("diceTitle", true)
            put("sideNumber", true)
            put("behaviour", true)
            put("sideDescription", true)
            put("sideSVG", true)
            put("rollSound", true)
        }
        root.add(settings)

        val bag = mapper.createObjectNode()
        val diceArray = bag.putArray("dice")
        diceArray.addObject().apply {
            put("title", "Legacy Dice")
            put("colour", "FFFFFFFF")
            put("epoch", "123456789")
            put("selected", true)
            val sides = putArray("side")
            repeat(6) {
                sides.addObject().apply {
                    put("number", 1)
                    put("numberColour", "FFFFFFFF")
                    put("descriptionColour", "FFFFFFFF")
                }
            }
        }
        root.add(bag)

        val rolls = mapper.createObjectNode().putArray("values")
        root.add(rolls)

        val groups = mapper.createObjectNode().putArray("group")
        root.add(groups)
        return root
    }

    @Test
    fun validateSuccessV250() {
        val root = createValidRootV250()
        val validator = RepositoryImportValidation()
        validator.validate(root)
    }

    @Test
    fun validateSuccessV260() {
        val root = createValidRootV260()
        val validator = RepositoryImportValidation()
        validator.validate(root)
    }

    @Test
    fun validateDuplicateDiceTitles() {
        val root = createValidRootV250()
        val diceArray = root.get("bag").get("dice") as ArrayNode
        diceArray.add(createValidDiceV250("Dice")) // Duplicate title

        val validator = RepositoryImportValidation()
        val exception = assertThrows(RepositoryImportException::class.java) {
            validator.validate(root)
        }
        assertEquals(RepositoryImportStatus.JSON_DICE_TITLE, exception.detail)
    }

    @Test
    fun validateInvalidSideCountLowerBound() {
        val root = createValidRootV250()
        val dice = (root.get("bag").get("dice") as ArrayNode).get(0) as ObjectNode
        val sides = dice.get("side") as ArrayNode
        sides.removeAll()
        sides.add(createValidSideV250()) // Only 1 side, invalid

        val validator = RepositoryImportValidation()
        val exception = assertThrows(RepositoryImportException::class.java) {
            validator.validate(root)
        }
        assertEquals(RepositoryImportStatus.JSON_SIDE_SIZE, exception.detail)
    }

    @Test
    fun validateInvalidSideCountUpperBound() {
        val root = createValidRootV250()
        val dice = (root.get("bag").get("dice") as ArrayNode).get(0) as ObjectNode
        val sides = dice.get("side") as ArrayNode
        sides.removeAll()
        repeat(1001) { sides.add(createValidSideV250()) } // 1001 sides, invalid

        val validator = RepositoryImportValidation()
        val exception = assertThrows(RepositoryImportException::class.java) {
            validator.validate(root)
        }
        assertEquals(RepositoryImportStatus.JSON_SIDE_SIZE, exception.detail)
    }

    @Test
    fun validateValidSideCountBounds() {
        val root = createValidRootV250()
        val dice = (root.get("bag").get("dice") as ArrayNode).get(0) as ObjectNode
        val sides = dice.get("side") as ArrayNode
        val validator = RepositoryImportValidation()

        // Lower bound: 2 sides
        sides.removeAll()
        repeat(2) { sides.add(createValidSideV250()) }
        validator.validate(root)

        // Upper bound: 1000 sides
        sides.removeAll()
        repeat(1000) { sides.add(createValidSideV250()) }
        validator.validate(root)
    }

    @Test
    fun validateRollDiceUuidNotFound() {
        val root = createValidRootV250()
        val rollsValues = (root.get("rolls") as ObjectNode).putArray("values")
        val rollGroup = rollsValues.addObject()
        val rolls = rollGroup.putArray("roll")
        val roll = rolls.addObject()
        roll.put("uuidDice", UUID.randomUUID().toString()) // Random UUID not in bag

        val validator = RepositoryImportValidation()
        val exception = assertThrows(RepositoryImportException::class.java) {
            validator.validate(root)
        }
        assertEquals(RepositoryImportStatus.JSON_DICE_UUID, exception.detail)
    }

    @Test
    fun validateGroupDiceUuidNotFound() {
        val root = createValidRootV250()
        val groups = root.get("groups").get("group") as ArrayNode
        val group = groups.addObject()
        group.put("uuid", UUID.randomUUID().toString())
        group.put("name", "Group 1")
        group.putArray("uuidDice").add(UUID.randomUUID().toString()) // Random UUID not in bag
        group.put("notes", "")
        group.put("displayIndex", 0)
        group.put("selected", true)

        val validator = RepositoryImportValidation()
        val exception = assertThrows(RepositoryImportException::class.java) {
            validator.validate(root)
        }
        assertEquals(RepositoryImportStatus.JSON_DICE_UUID, exception.detail)
    }

    @Test
    fun validateFutureVersion() {
        val root = createValidRootV250()
        root.put("version", "3.0.0")

        val validator = RepositoryImportValidation()
        val exception = assertThrows(RepositoryImportException::class.java) {
            validator.validate(root)
        }
        assertEquals(RepositoryImportStatus.JSON_FILE_UNKNOWN_VERSION, exception.detail)
    }

    @Test
    fun validateVersionComparison() {
        val root = createValidRootV260()
        val validator = RepositoryImportValidation()

        root.put("version", "2.6.0")
        root.set<ObjectNode>("settings", createValidSettingsV260())
        validator.validate(root)

        root.put("version", "2.5.0")
        root.set<ObjectNode>("settings", createValidSettingsV250())
        validator.validate(root)

        root.put("version", "2.5.1")
        root.set<ObjectNode>("settings", createValidSettingsV250())
        validator.validate(root)

        root.put("version", "2.5.2")
        root.set<ObjectNode>("settings", createValidSettingsV250())
        validator.validate(root)

        root.put("version", "0.0.0")
        assertThrows(RepositoryImportException::class.java) {
            validator.validate(root)
        }
    }

    @Test
    fun validateLegacyFormat() {
        val root = createValidLegacyRoot()
        val validator = RepositoryImportValidation()
        validator.validate(root)
    }

    @Test
    fun validateLegacyInvalidSize() {
        val root = mapper.createArrayNode()
        root.add(mapper.createObjectNode())
        root.add(mapper.createObjectNode())
        // Only 2 elements, should fail (legacy expects 3 or 4)

        val validator = RepositoryImportValidation()
        val exception = assertThrows(RepositoryImportException::class.java) {
            validator.validate(root)
        }
        assertEquals(RepositoryImportStatus.JSON_FILE_MISSING_SECTION, exception.detail)
    }

    @Test
    fun validateRollSide() {
        val root = createValidRootV250()
        val rollsValues = (root.get("rolls") as ObjectNode).putArray("values")
        val rollGroup = rollsValues.addObject()
        val rolls = rollGroup.putArray("roll")
        val roll = rolls.addObject()
        val diceUuid = (root.get("bag").get("dice") as ArrayNode).get(0).get("uuid").asText()
        roll.put("uuidDice", diceUuid)
        val side = roll.putObject("side")
        side.put("uuid", UUID.randomUUID().toString())
        side.put("number", 1)
        side.put("numberColour", "INVALID_COLOUR") // Schema validation for side should fail

        val validator = RepositoryImportValidation()
        val exception = assertThrows(RepositoryImportException::class.java) {
            validator.validate(root)
        }
        assertEquals(RepositoryImportStatus.JSON_SCHEMA_SIDE, exception.detail)
    }

    @Test
    fun validateJsonVersionPopulated() {
        // Versioned 2.5.0
        val rootV250 = createValidRootV250()
        val validatorV250 = RepositoryImportValidation()
        validatorV250.validate(rootV250)
        assertEquals("2.5.0", validatorV250.jsonVersion)

        // Versioned 2.6.0
        val rootV260 = createValidRootV260()
        val validatorV260 = RepositoryImportValidation()
        validatorV260.validate(rootV260)
        assertEquals("2.6.0", validatorV260.jsonVersion)

        // Legacy
        val rootLegacy = createValidLegacyRoot()
        val validatorLegacy = RepositoryImportValidation()
        validatorLegacy.validate(rootLegacy)
        assertEquals("legacy", validatorLegacy.jsonVersion)
    }
}
