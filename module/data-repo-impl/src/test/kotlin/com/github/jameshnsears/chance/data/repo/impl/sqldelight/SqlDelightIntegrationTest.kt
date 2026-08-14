package com.github.jameshnsears.chance.data.repo.impl.sqldelight

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SqlDelightIntegrationTest {
    @Test
    fun testDatabase() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ChanceDatabase.Schema.create(driver)
        val database = ChanceDatabase(driver)
        val settingsQueries = database.settingsQueries

        settingsQueries.saveSettings(
            rollIndexTime = true,
            rollScore = false,
            diceTitle = true,
            sideNumber = false,
            behaviour = true,
            sideDescription = false,
            sideSVG = true,
            rollSound = false,
            shuffle = true,
            haptics = false,
            rollScoreTTS = true,
            shakeToRoll = false,
            resizeZoom = 1.5,
            groupTitle = true,
            layout = false,
            history = true,
        )

        var settings = settingsQueries.getSettings().executeAsOne()
        assertEquals(true, settings.rollIndexTime)
        assertEquals(1.5, settings.resizeZoom, 0.0)
        assertEquals(true, settings.history)

        settingsQueries.updateResizeZoom(2.0)
        settings = settingsQueries.getSettings().executeAsOne()
        assertEquals(2.0, settings.resizeZoom, 0.0)
    }

    @Test
    fun testFlows() = runBlocking {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ChanceDatabase.Schema.create(driver)
        val database = ChanceDatabase(driver)
        val settingsQueries = database.settingsQueries

        settingsQueries.saveSettings(
            rollIndexTime = true,
            rollScore = false,
            diceTitle = true,
            sideNumber = false,
            behaviour = true,
            sideDescription = false,
            sideSVG = true,
            rollSound = false,
            shuffle = true,
            haptics = false,
            rollScoreTTS = true,
            shakeToRoll = false,
            resizeZoom = 1.0,
            groupTitle = true,
            layout = false,
            history = true,
        )

        val settingsFlow = settingsQueries.getSettings().asFlow().mapToOne(Dispatchers.IO)

        val initialSettings = settingsFlow.first()
        assertEquals(1.0, initialSettings.resizeZoom, 0.0)

        settingsQueries.updateResizeZoom(3.0)

        val updatedSettings = settingsFlow.first { it.resizeZoom == 3.0 }
        assertEquals(3.0, updatedSettings.resizeZoom, 0.0)
    }
}
