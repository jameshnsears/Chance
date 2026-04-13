package com.github.jameshnsears.chance.data.repo.api.settings

import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.repo.api.RepositoryImportExportInterface
import kotlinx.coroutines.flow.Flow

interface RepositorySettingsInterface : RepositoryImportExportInterface {
    suspend fun fetch(): Flow<SettingsDataInterface>
    suspend fun store(settingsData: SettingsDataInterface)
}
