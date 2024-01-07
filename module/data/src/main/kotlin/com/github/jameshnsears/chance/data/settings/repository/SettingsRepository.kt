package com.github.jameshnsears.chance.data.settings.repository

import android.content.Context
import com.github.jameshnsears.chance.data.domain.Settings
import com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

object SettingsRepository : SettingsRepositoryInterface {
    override suspend fun fetch(context: Context?): Flow<Settings> = flow {
        val settings: Settings = context!!.settingsDataStore.data
            .map { settingsProtocolBuffer ->
                // map SettingsProtocolBuffer into Settings
                Settings(bagZoom = settingsProtocolBuffer.bagZoom)
            }.first()

        emit(settings)

    }

//    suspend fun fetch(context: Context): SettingsProtocolBuffer
//        = context.settingsDataStore.data.first()

    override suspend fun store(context: Context?, newSettings: Settings) {
        context!!.settingsDataStore.updateData { currentSettingsProtocolBuffer ->
            currentSettingsProtocolBuffer.toBuilder()
                .setBagZoom(newSettings.bagZoom)
                .build()
        }
    }

    suspend fun empty(context: Context) {
        context.settingsDataStore.updateData {
            SettingsProtocolBuffer.newBuilder().build()
        }
    }
}