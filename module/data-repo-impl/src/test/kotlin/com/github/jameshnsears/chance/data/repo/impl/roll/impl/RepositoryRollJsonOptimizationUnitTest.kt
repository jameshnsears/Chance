package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import android.content.Context
import androidx.datastore.core.DataStore
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.impl.roll.testdouble.RepositoryRollProtocolBufferTestDouble
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryRollJsonOptimizationUnitTest : UtilityAndroidUnitTestHelper() {
    private val context = mockk<Context>()
    private val dataStore = mockk<DataStore<RollHistoryProtocolBuffer>>()
    private val repositoryBag = mockk<RepositoryBagInterface>()
    private val IMAGE_BASE64 =
        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg"

    @Before
    fun setUp() {
        val instanceField = RepositoryRollProtocolBufferImpl::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)

        val testDoubleInstanceField = RepositoryRollProtocolBufferTestDouble::class.java.getDeclaredField("instance")
        testDoubleInstanceField.isAccessible = true
        testDoubleInstanceField.set(null, null)

        mockkStatic("com.github.jameshnsears.chance.data.repo.impl.roll.impl.RepositoryRollProtocolBufferImplKt")
        every { context.rollDataStore } returns dataStore
        coEvery { dataStore.data } returns flowOf(RollHistoryProtocolBuffer.getDefaultInstance())
        coEvery { repositoryBag.fetch() } returns flowOf(mutableListOf())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun jsonExportImportOptimizationImpl() = runTest {
        val updateDataSlot = slot<suspend (RollHistoryProtocolBuffer) -> RollHistoryProtocolBuffer>()
        val dataStoreFlow = MutableStateFlow(RollHistoryProtocolBuffer.getDefaultInstance())

        coEvery { dataStore.updateData(capture(updateDataSlot)) } coAnswers {
            val nextState = updateDataSlot.captured.invoke(dataStoreFlow.value)
            dataStoreFlow.value = nextState
            nextState
        }
        coEvery { dataStore.data } returns dataStoreFlow

        val side1 = Side(
            uuid = "s1",
            number = 6,
            imageBase64 = IMAGE_BASE64,
            description = "Side Six"
        )
        val side2 = Side(uuid = "s2", number = 1)
        val bag = mutableListOf(Dice(sides = listOf(side1, side2)))
        coEvery { repositoryBag.fetch() } returns flowOf(bag)

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)

        val roll1 = Roll(uuidDice = "d1", side = side1, score = 6)
        val roll2 = Roll(uuidDice = "d1", side = side1, score = 6)

        repository.store(1L, listOf(roll1, roll2))

        val json = repository.jsonExport()

        // Assertions for Exported JSON
        assertTrue("JSON should NOT contain the cache entry -1", !json.contains("\"-1\""))
        assertTrue("Rolls should contain side uuid", json.contains("\"uuid\": \"s1\""))
        assertFalse("Rolls should NOT contain side number", json.contains("\"number\": 6"))
        assertFalse("Rolls should NOT contain description", json.contains("\"description\": \"Side Six\""))

        // Import back
        repository.clear()
        repository.jsonImport(json)

        val importedHistory = repository.fetch().first()
        assertEquals(1, importedHistory.size)
        val importedRolls = importedHistory[1L]!!
        assertEquals(2, importedRolls.size)

        val importedSide = importedRolls[0].side
        assertEquals("s1", importedSide.uuid)
        assertEquals(6, importedSide.number)
        assertEquals(IMAGE_BASE64, importedSide.imageBase64)
        assertEquals("Side Six", importedSide.description)
    }

    @Test
    fun jsonExportImportOptimizationTestDouble() = runTest {
        val side1 = Side(
            uuid = "s1",
            number = 6,
            imageBase64 = IMAGE_BASE64,
            description = "Side Six"
        )
        val side2 = Side(uuid = "s2", number = 1)
        val bag = mutableListOf(Dice(sides = listOf(side1, side2)))
        coEvery { repositoryBag.fetch() } returns flowOf(bag)

        val repository = RepositoryRollProtocolBufferTestDouble.getInstance(LinkedHashMap(), repositoryBag)
        repository.clear()

        val roll1 = Roll(uuidDice = "d1", side = side1, score = 6)
        val roll2 = Roll(uuidDice = "d1", side = side1, score = 6)

        repository.store(1L, listOf(roll1, roll2))

        val json = repository.jsonExport()

        // Assertions for Exported JSON
        assertTrue("JSON should NOT contain the cache entry -1", !json.contains("\"-1\""))
        assertTrue("Rolls should contain side uuid", json.contains("\"uuid\": \"s1\""))
        assertFalse("Rolls should NOT contain side number", json.contains("\"number\": 6"))
        assertFalse("Rolls should NOT contain description", json.contains("\"description\": \"Side Six\""))

        // Import back
        repository.clear()
        repository.jsonImport(json)

        val importedHistory = repository.fetch().first()
        assertEquals(1, importedHistory.size)
        val importedRolls = importedHistory[1L]!!
        assertEquals(2, importedRolls.size)

        val importedSide = importedRolls[0].side
        assertEquals("s1", importedSide.uuid)
        assertEquals(6, importedSide.number)
        assertEquals(IMAGE_BASE64, importedSide.imageBase64)
        assertEquals("Side Six", importedSide.description)
    }
}
