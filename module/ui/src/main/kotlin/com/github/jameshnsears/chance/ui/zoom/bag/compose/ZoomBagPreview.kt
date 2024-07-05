package com.github.jameshnsears.chance.ui.zoom.bag.compose

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.domain.state.Settings
import com.github.jameshnsears.chance.data.repository.bag.testdouble.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.testdouble.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.testdouble.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.data.sample.roll.SampleRollTestData
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@UtilityPreview
@Composable
fun ZoomBagPreview() {
    val sampleBagTestData = SampleBagTestData()

    val repositorySettings = RepositorySettingsTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositorySettings.store(Settings())
    }

    val repositoryBag = RepositoryBagTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryBag.store(sampleBagTestData.allDice)
    }

    val sampleRollTestData = SampleRollTestData(sampleBagTestData)

    val repositoryRoll = RepositoryRollTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryRoll.store(sampleRollTestData.rollHistory)
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            ZoomBag(
                ZoomAndroidViewModel(
                    mockk<Application>(),
                    repositoryBag,
                    repositoryRoll
                ),
            )
        }
    }
}
