package com.github.jameshnsears.chance.data.settings.repository

import android.content.Context
import com.github.jameshnsears.chance.data.domain.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepositoryInterface {
    suspend fun fetch(context: Context?): Flow<Settings>
    suspend fun store(context: Context?, newSettings: Settings)
}
