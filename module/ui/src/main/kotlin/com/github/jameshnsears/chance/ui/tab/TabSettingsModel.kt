package com.github.jameshnsears.chance.ui.tab

import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import kotlinx.coroutines.flow.first

enum class SettingsTab {
    TAB_DICE,
    TAB_ROLLS
}

class TabSettingsModel {
    companion object {
        suspend fun resize(repositorySettings: RepositorySettingsInterface) =
            repositorySettings.fetch().first().resize

        suspend fun resize(repositorySettings: RepositorySettingsInterface, resize: Float) {
            val settings = repositorySettings.fetch().first()
            settings.resize = resize
            repositorySettings.store(settings)
        }

        suspend fun markTabAsCurrentInSettings(repositorySettings: RepositorySettingsInterface) =
            repositorySettings.fetch().first().tabRowChance

        suspend fun markTabAsCurrentInSettings(
            repositorySettings: RepositorySettingsInterface,
            tabRowChance: SettingsTab
        ) {
            val settings = repositorySettings.fetch().first()
            settings.tabRowChance =
                when (tabRowChance) {
                    SettingsTab.TAB_DICE -> 0
                    else -> 1
                }
            repositorySettings.store(settings)
        }
    }
}
