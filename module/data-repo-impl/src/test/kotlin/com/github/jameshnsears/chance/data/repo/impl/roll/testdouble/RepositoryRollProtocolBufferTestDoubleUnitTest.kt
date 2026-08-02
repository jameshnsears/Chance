package com.github.jameshnsears.chance.data.repo.impl.roll.testdouble

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryRollProtocolBufferTestDoubleUnitTest : UtilityAndroidUnitTestHelper() {
    private val repositoryBag = mockk<RepositoryBagInterface>()

    @Before
    fun setUp() {
        val instanceField = RepositoryRollProtocolBufferTestDouble::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)

        coEvery { repositoryBag.fetch() } returns flowOf(mutableListOf())
    }

    @Test
    fun storeIncremental() = runTest {
        val repository = RepositoryRollProtocolBufferTestDouble.getInstance(linkedMapOf(), repositoryBag)

        repository.store(1L, listOf(Roll(uuidDice = "d1", side = Side(number = 1))))
        repository.store(2L, listOf(Roll(uuidDice = "d2", side = Side(number = 2))))

        val history = repository.fetch().first()
        assertEquals(2, history.size)
        assertTrue(history.containsKey(1L))
        assertTrue(history.containsKey(2L))
    }

    @Test
    fun removeLatest() = runTest {
        val repository = RepositoryRollProtocolBufferTestDouble.getInstance(linkedMapOf(), repositoryBag)

        repository.store(1L, listOf(Roll(uuidDice = "d1", side = Side(number = 1))))
        repository.store(2L, listOf(Roll(uuidDice = "d2", side = Side(number = 2))))

        repository.removeLatest()

        val history = repository.fetch().first()
        assertEquals(1, history.size)
        assertTrue(history.containsKey(1L))
        assertTrue(!history.containsKey(2L))
    }

    @Test
    fun jsonExport() = runTest {
        val sideUuid = "uuidSide"
        val rollHistory = linkedMapOf(
            123456789L to listOf(
                Roll(
                    uuidDice = "uuidDice",
                    side = Side(
                        uuid = sideUuid,
                        number = 6,
                        imageBase64 = "imageBase64Data",
                        description = "sideDescription"
                    ),
                    score = 6
                )
            )
        )
        val repository = RepositoryRollProtocolBufferTestDouble.getInstance(rollHistory, repositoryBag)
        val json = repository.jsonExport()

        assertTrue(json.contains("\"uuid\": \"uuidSide\""))
        assertFalse(json.contains("\"number\": 6"))
        assertFalse(json.contains("\"imageBase64\": \"imageBase64Data\""))
        assertFalse(json.contains("\"description\": \"sideDescription\""))
    }

    @Test
    fun jsonImport() = runTest {
        val sideUuid = "uuidSide"
        val bag = mutableListOf(
            Dice(
                sides = listOf(
                    Side(uuid = sideUuid, number = 6, imageBase64 = "imageBase64Data", description = "sideDescription")
                )
            )
        )
        coEvery { repositoryBag.fetch() } returns flowOf(bag)

        val repository = RepositoryRollProtocolBufferTestDouble.getInstance(linkedMapOf(), repositoryBag)
        val json = """
            {
              "values": {
                "123456789": {
                  "roll": [
                    {
                      "uuidDice": "uuidDice",
                      "side": {
                        "uuid": "$sideUuid"
                      },
                      "score": 6
                    }
                  ]
                }
              }
            }
        """.trimIndent()
        repository.jsonImport(json)

        val result = repository.fetch().first()
        assertEquals(1, result.size)
        val rolls = result[123456789L]!!
        assertEquals(1, rolls.size)
        assertEquals(sideUuid, rolls[0].side.uuid)
        assertEquals(6, rolls[0].side.number)
        assertEquals("imageBase64Data", rolls[0].side.imageBase64)
    }
}
