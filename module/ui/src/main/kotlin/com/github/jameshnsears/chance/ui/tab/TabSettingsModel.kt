package com.github.jameshnsears.chance.ui.tab

import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import kotlinx.coroutines.flow.first

class TabSettingsModel {
    companion object {
        suspend fun resize(repositorySettings: RepositorySettingsInterface) =
            repositorySettings.fetch().first().resize

        suspend fun resize(repositorySettings: RepositorySettingsInterface, resize: Int) {
            val settings = repositorySettings.fetch().first()
            settings.resize = resize
            repositorySettings.store(settings)
        }
    }
}
