package com.github.jameshnsears.chance.data.repo.impl.settings.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.ChanceDatabase
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext

class RepositorySettingsSqlDelightImpl(
    private val database: ChanceDatabase
) : RepositorySettingsInterface {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val settingsFlow = database.settingsQueries.getSettings()
        .asFlow()
        .mapToOne(Dispatchers.IO)
        .map { it.toSettingsData() }
        .shareIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    override fun fetch(): Flow<SettingsDataInterface> = settingsFlow

    override suspend fun store(settingsData: SettingsDataInterface) {
        withContext(Dispatchers.IO) {
            database.settingsQueries.saveSettings(
                rollIndexTime = settingsData.rollIndexTime,
                rollScore = settingsData.rollScore,
                diceTitle = settingsData.diceTitle,
                sideNumber = settingsData.sideNumber,
                behaviour = settingsData.rollBehaviour,
                sideDescription = settingsData.sideDescription,
                sideSVG = settingsData.sideSVG,
                rollSound = settingsData.rollSound,
                shuffle = settingsData.shuffle,
                haptics = settingsData.haptics,
                rollScoreTTS = settingsData.rollScoreTTS,
                shakeToRoll = settingsData.shakeToRoll,
                resizeZoom = settingsData.resizeZoom.toDouble(),
                groupTitle = settingsData.groupTitle,
                layout = settingsData.layout,
                history = settingsData.history
            )
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.settingsQueries.clearSettings()
        }
    }

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
        val settings = fetch().first()
        jacksonObjectMapper().writeValueAsString(settings)
    }

    override suspend fun jsonImport(json: String) {
        val settingsData = jacksonObjectMapper().readValue(json, SettingsDataImpl::class.java)
        store(settingsData)
    }

    private fun Settings.toSettingsData(): SettingsDataInterface {
        return SettingsDataImpl(
            rollIndexTime = rollIndexTime,
            rollScore = rollScore,
            diceTitle = diceTitle,
            sideNumber = sideNumber,
            rollBehaviour = behaviour,
            sideDescription = sideDescription,
            sideSVG = sideSVG,
            rollSound = rollSound,
            shuffle = shuffle,
            haptics = haptics,
            rollScoreTTS = rollScoreTTS,
            shakeToRoll = shakeToRoll,
            resizeZoom = resizeZoom.toFloat(),
            groupTitle = groupTitle,
            layout = layout,
            history = history
        )
    }
}
