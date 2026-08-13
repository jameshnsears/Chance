package com.github.jameshnsears.chance.data.repo.impl.bag.testdouble

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryBagProtocolBufferTestDoubleUnitTest {
    @Before
    fun setUp() {
        val instanceField = RepositoryBagProtocolBufferTestDouble::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)
    }

    @Test
    fun jsonExport() = runTest {
        val diceBag = mutableListOf(
            Dice(
                uuid = "uuid",
                title = "d6",
                selected = false,
                explode = false,
                displayIndex = 0,
                modifyScore = false,
                sides = mutableListOf(
                    Side(
                        imageBase64 = "",
                        description = ""
                    )
                )
            )
        )
        val repository = RepositoryBagProtocolBufferTestDouble.getInstance()
        repository.store(diceBag)
        val json = repository.jsonExport()

        assertTrue(json.contains("\"title\": \"d6\""))
        assertTrue(json.contains("\"selected\": false"))
        assertTrue(json.contains("\"explode\": false"))
        assertTrue(json.contains("\"displayIndex\": 0"))
        assertTrue(json.contains("\"modifyScore\": false"))
        assertTrue(json.contains("\"imageDrawableId\": 0"))
        assertTrue(json.contains("\"imageBase64\": \"\""))
        assertTrue(json.contains("\"description\": \"\""))
    }

    @Test
    fun imageUuidReference() = runTest {
        val sideWithImage = Side(
            uuid = "uuid-with-image",
            imageBase64 = "base64-data"
        )
        val sideReferencingImage = Side(
            uuid = "uuid-referencing-image",
            imageBase64 = "",
            imageBase64Uuid = "uuid-with-image"
        )

        val diceBag = mutableListOf(
            Dice(
                uuid = "dice-uuid",
                sides = mutableListOf(sideWithImage, sideReferencingImage)
            )
        )

        val repository = RepositoryBagProtocolBufferTestDouble.getInstance()
        repository.store(diceBag)

        val fetchedBag = repository.fetch().first()
        val sides = fetchedBag.first().sides
        val fetchedSideReferencingImage = sides.find { it.uuid == "uuid-referencing-image" }

        assertTrue(
            "imageBase64 should be populated from referenced UUID",
            fetchedSideReferencingImage?.imageBase64 == "base64-data"
        )
    }

    @Test
    fun jsonExportDeduplication() = runTest {
        val imageBase64Data = "shared-image-data"
        val side1 = Side(
            uuid = "side-1",
            imageBase64 = imageBase64Data
        )
        val side2 = Side(
            uuid = "side-2",
            imageBase64 = imageBase64Data
        )

        val diceBag = mutableListOf(
            Dice(
                uuid = "dice-uuid",
                sides = mutableListOf(side1, side2)
            )
        )

        val repository = RepositoryBagProtocolBufferTestDouble.getInstance()
        repository.store(diceBag)
        val json = repository.jsonExport()

        // side-1 should have imageBase64 but no imageBase64Uuid
        assertTrue(
            "side-1 should have imageBase64",
            json.contains("\"uuid\": \"side-1\"") && json.contains("\"imageBase64\": \"shared-image-data\"")
        )

        // side-2 should have imageBase64Uuid pointing to side-1, and empty imageBase64
        // Protocol buffer JSON format might omit empty strings if not specified,
        // but we included it in fieldsToAlwaysOutput.
        assertTrue(
            "side-2 should have imageBase64Uuid",
            json.contains("\"uuid\": \"side-2\"") && json.contains("\"imageBase64Uuid\": \"side-1\"")
        )

        // Ensure mutual exclusivity (check side-1 does not have imageBase64Uuid and side-2 does not have imageBase64)
        // This is a bit tricky with raw string contains, but let's try to be specific
        val side1Block = json.substringAfter("\"uuid\": \"side-1\"").substringBefore("}")
        val side2Block = json.substringAfter("\"uuid\": \"side-2\"").substringBefore("}")

        assertTrue("side-1 should not have imageBase64Uuid", !side1Block.contains("imageBase64Uuid"))
        assertTrue("side-2 should have empty imageBase64", side2Block.contains("\"imageBase64\": \"\""))
    }

    @Test
    fun storeTargeted() = runTest {
        val diceUuid = "dice-uuid"
        val diceBag = mutableListOf(
            Dice(
                uuid = diceUuid,
                selected = false
            )
        )
        val repository = RepositoryBagProtocolBufferTestDouble.getInstance()
        repository.store(diceBag)

        repository.store(diceUuid, true)
        val fetchedBag = repository.fetch().first()
        assertTrue(fetchedBag.first().selected)

        repository.store(diceUuid, false)
        val fetchedBag2 = repository.fetch().first()
        assertTrue(!fetchedBag2.first().selected)
    }

    @Test
    fun fetchReferencedSideUuids() = runTest {
        val diceBag = mutableListOf(
            Dice(
                uuid = "dice-uuid",
                sides = mutableListOf(
                    Side(uuid = "side-1"),
                    Side(uuid = "side-2"),
                )
            )
        )
        val repository = RepositoryBagProtocolBufferTestDouble.getInstance()
        repository.store(diceBag)

        assertTrue(repository.fetchReferencedSideUuids() == setOf("side-1", "side-2"))
    }

    @Test
    fun fetchSelected() = runTest {
        val diceUuid = "dice-uuid"
        val diceBag = mutableListOf(
            Dice(
                uuid = diceUuid,
                selected = true
            ),
            Dice(
                uuid = "other-uuid",
                selected = false
            )
        )
        val repository = RepositoryBagProtocolBufferTestDouble.getInstance()
        repository.store(diceBag)

        val selectedUuids = repository.fetchSelected().first()
        assertTrue(selectedUuids.size == 1)
        assertTrue(selectedUuids.contains(diceUuid))

        repository.store(diceUuid, false)
        val selectedUuids2 = repository.fetchSelected().first()
        assertTrue(selectedUuids2.isEmpty())
    }
}
