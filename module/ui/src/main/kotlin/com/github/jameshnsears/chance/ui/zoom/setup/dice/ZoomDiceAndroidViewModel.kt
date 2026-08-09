package com.github.jameshnsears.chance.ui.zoom.setup.dice

import android.app.Application
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel

class ZoomDiceAndroidViewModel(
    application: Application,
    repositorySettings: RepositorySettingsInterface,
    repositoryBag: RepositoryBagInterface,
    repositoryRoll: RepositoryRollInterface,
) : ZoomAndroidViewModel(
    application,
    repositorySettings,
    repositoryBag,
    repositoryRoll,
)
