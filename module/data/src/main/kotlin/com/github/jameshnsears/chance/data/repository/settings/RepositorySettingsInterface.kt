package com.github.jameshnsears.chance.data.repository.settings

import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.repository.RepositoryImportExportInterface
import kotlinx.coroutines.flow.Flow

interface RepositorySettingsInterface : RepositoryImportExportInterface {
    suspend fun fetch(): Flow<SettingsDataInterface>
    suspend fun store(settingsData: SettingsDataInterface)
}
