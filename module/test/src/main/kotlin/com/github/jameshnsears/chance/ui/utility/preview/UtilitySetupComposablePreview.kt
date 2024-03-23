package com.github.jameshnsears.chance.ui.utility.preview

import android.app.Application
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.mock.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.mock.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagStartup
import com.github.jameshnsears.chance.data.sample.roll.SampleRollSampleBagStartup
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun getViewModels(): Pair<TabBagAndroidViewModel, ZoomAndroidViewModel> {
    val repositorySettings = RepositorySettingsTestDouble.getInstance()

    val repositoryBag = RepositoryBagTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryBag.store(SampleBagStartup.allDice)
    }

    val repositoryRoll = RepositoryRollTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryRoll.store(SampleRollSampleBagStartup.rollHistory)
    }

    return Pair(
        TabBagAndroidViewModel(
            mockk<Application>(),
            repositorySettings,
            repositoryBag,
            repositoryRoll
        ),
        ZoomAndroidViewModel(
            mockk<Application>(),
            repositoryBag,
            repositoryRoll
        )
    )
}
