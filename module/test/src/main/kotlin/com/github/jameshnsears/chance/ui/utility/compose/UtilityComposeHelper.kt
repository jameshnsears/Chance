package com.github.jameshnsears.chance.ui.utility.compose

import android.app.Application
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.mock.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.mock.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.data.sample.roll.SampleRollTestData
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun getViewModels(): Pair<TabBagAndroidViewModel, ZoomAndroidViewModel> {
    val repositorySettings = RepositorySettingsTestDouble.getInstance()

    val sampleBagTestData = SampleBagTestData()
    val repositoryBag = RepositoryBagTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryBag.store(sampleBagTestData.allDice)
    }

    val sampleRollTestData = SampleRollTestData(sampleBagTestData)
    val repositoryRoll = RepositoryRollTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryRoll.store(sampleRollTestData.rollHistory)
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
