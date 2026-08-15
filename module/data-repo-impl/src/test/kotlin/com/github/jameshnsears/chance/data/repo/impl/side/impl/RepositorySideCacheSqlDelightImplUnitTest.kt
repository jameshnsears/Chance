package com.github.jameshnsears.chance.data.repo.impl.side.impl

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.ChanceDatabase
import com.github.jameshnsears.chance.data.repo.impl.utility.SqlDelightTestDriver
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.UUID
import kotlin.io.encoding.ExperimentalEncodingApi

class RepositorySideCacheSqlDelightImplUnitTest : UtilityAndroidUnitTestHelper() {
    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun storeAndFetchSvg() = runTest {
        val driver = SqlDelightTestDriver.create()
        val database = ChanceDatabase(driver)
        val repository = RepositorySideCacheSqlDelightImpl(database)

        val uuid = UUID.randomUUID().toString()
        // Base64 encoded: <svg viewBox="0 0 10 10"><circle cx="5" cy="5" r="4"/></svg>
        val svgBase64 = "PHN2ZyB2aWV3Qm94PSIwIDAgMTAgMTAiPjxjaXJjbGUgY3g9IjUiIGN5PSI1IiByPSI0Ii8+PC9zdmc+"
        val expectedSvg = "<svg viewBox=\"0 0 10 10\"><circle cx=\"5\" cy=\"5\" r=\"4\"/></svg>"

        repository.store(uuid, svgBase64)

        val cache = repository.fetch().first()
        assertEquals(expectedSvg, cache[uuid])

        // Verify database state directly
        val image = database.imageQueries.getAllImages().executeAsList().first { it.uuid == uuid }
        assertNull("base64 field should be null for SVG", image.base64)
        assertEquals(expectedSvg, image.svg)

        driver.close()
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun storeAndFetchNonSvg() = runTest {
        val driver = SqlDelightTestDriver.create()
        val database = ChanceDatabase(driver)
        val repository = RepositorySideCacheSqlDelightImpl(database)

        val uuid = UUID.randomUUID().toString()
        // Base64 encoded: "not an svg"
        val nonSvgBase64 = "bm90IGFuIHN2Zw=="

        repository.store(uuid, nonSvgBase64)

        val cache = repository.fetch().first()
        assertEquals(nonSvgBase64, cache[uuid])

        // Verify database state directly
        val image = database.imageQueries.getAllImages().executeAsList().first { it.uuid == uuid }
        assertEquals(nonSvgBase64, image.base64)
        assertNull("svg field should be null for non-SVG", image.svg)

        driver.close()
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun storeMapAndFetch() = runTest {
        val driver = SqlDelightTestDriver.create()
        val database = ChanceDatabase(driver)
        val repository = RepositorySideCacheSqlDelightImpl(database)

        val uuid1 = UUID.randomUUID().toString()
        val svgBase64 = "PHN2ZyB2aWV3Qm94PSIwIDAgMTAgMTAiPjxjaXJjbGUgY3g9IjUiIGN5PSI1IiByPSI0Ii8+PC9zdmc+"
        val expectedSvg = "<svg viewBox=\"0 0 10 10\"><circle cx=\"5\" cy=\"5\" r=\"4\"/></svg>"

        val uuid2 = UUID.randomUUID().toString()
        val nonSvgBase64 = "bm90IGFuIHN2Zw=="

        repository.store(mapOf(uuid1 to svgBase64, uuid2 to nonSvgBase64))

        val cache = repository.fetch().first()
        assertEquals(expectedSvg, cache[uuid1])
        assertEquals(nonSvgBase64, cache[uuid2])

        driver.close()
    }
}
