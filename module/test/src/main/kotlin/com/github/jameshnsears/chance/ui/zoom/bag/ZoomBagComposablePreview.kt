package com.github.jameshnsears.chance.ui.zoom.bag

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.bag.BagSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview(heightDp = 400, widthDp = 360)
@Composable
fun ZoomBagComposablePreviewPortrait() {
    val settingsRepository = SettingsRepositoryTestDouble.getInstance()

    val bagRepository = BagRepositoryTestDouble.getInstance()
    bagRepository.store(BagDemoSampleData.allDice)

    val zoomBagViewModel = ZoomBagViewModel(settingsRepository, bagRepository)

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ZoomBag(zoomBagViewModel)
        }
    }
}

@Preview(heightDp = 900, widthDp = 1000)
@Composable
fun ZoomBagComposablePreviewLandscape() {
    val settingsRepository = SettingsRepositoryTestDouble.getInstance()

    val bagRepository = BagRepositoryTestDouble.getInstance()
    bagRepository.store(BagSampleData.allDice)

    val zoomBagViewModel = ZoomBagViewModel(settingsRepository, bagRepository)

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ZoomBag(zoomBagViewModel)
        }
    }
}