package com.github.jameshnsears.chance.ui.tab.roll


import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.data.sample.roll.SampleRollSampleBag
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@UtilityPreview
@Composable
fun TabRollPreview() {
    val repositorySettings = RepositorySettingsTestDouble.getInstance()

    val repositoryBag = RepositoryBagTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryBag.store(SampleBag.allDice)
    }

    val repositoryRoll = RepositoryRollTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryRoll.store(SampleRollSampleBag.rollHistory)
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TabRollLayout(
                TabRollViewModel(
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

@UtilityPreview
@Composable
fun TabRollBottomSheetLayoutPreview() {
    val repositorySettings = RepositorySettingsTestDouble.getInstance()

    val repositoryBag = RepositoryBagTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryBag.store(SampleBag.allDice)
    }

    val repositoryRoll = RepositoryRollTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryRoll.store(SampleRollSampleBag.rollHistory)
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TabRollBottomSheetLayout(
                TabRollViewModel(
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                ),
            )
        }
    }
}
