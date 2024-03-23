package com.github.jameshnsears.chance.ui.zoom.roll.compose

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.mock.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.mock.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagStartup
import com.github.jameshnsears.chance.data.sample.roll.SampleRollSampleBagStartup
import com.github.jameshnsears.chance.ui.tab.roll.TabRollViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


@UtilityPreview
@Composable
fun ZoomRollPreview() {
    val repositoryBagTestDouble = RepositoryBagTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryBagTestDouble.store(SampleBagStartup.allDice)
    }

    val repositoryRollTestDouble = RepositoryRollTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryRollTestDouble.store(SampleRollSampleBagStartup.rollHistory)
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            ZoomRoll(
                TabRollViewModel(
                    RepositorySettingsTestDouble.getInstance(),
                    repositoryBagTestDouble,
                    repositoryRollTestDouble
                ),
                ZoomAndroidViewModel(
                    mockk<Application>(),
                    repositoryBagTestDouble,
                    repositoryRollTestDouble
                ),
            )
        }
    }
}
