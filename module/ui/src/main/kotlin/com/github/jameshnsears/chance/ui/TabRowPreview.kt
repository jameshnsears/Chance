package com.github.jameshnsears.chance.ui.tab.compose

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.repository.bag.testdouble.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.testdouble.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.testdouble.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.data.sample.roll.SampleRollTestData
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@UtilityPreview
@Composable
fun TabRowPreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
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

            TabRow(
                TabBagAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                ),
                TabRollAndroidViewModel(
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
    }
}
