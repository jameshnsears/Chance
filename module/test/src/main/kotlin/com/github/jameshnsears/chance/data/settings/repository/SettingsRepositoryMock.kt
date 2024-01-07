package com.github.jameshnsears.chance.data.settings.repository

import android.content.Context
import com.github.jameshnsears.chance.data.domain.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object SettingsRepositoryMock : SettingsRepositoryInterface {
    private lateinit var settings: Settings

    override suspend fun fetch(context: Context?): Flow<Settings> = flow {
        emit(settings)
    }

    override suspend fun store(context: Context?, newSettings: Settings) {
        settings = newSettings
    }
}
