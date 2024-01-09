package com.github.jameshnsears.chance.ui.zoom.bag

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import com.github.jameshnsears.chance.data.settings.repository.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview(heightDp = 400, widthDp = 360)
@Composable
fun ZoomBagComposablePreviewPortrait() {
    val settingsRepository = SettingsRepositoryTestDouble

    val bagRepository = BagRepositoryTestDouble
    bagRepository.store(BagDemoData.dice)

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
    val settingsRepository = SettingsRepositoryTestDouble

    val bagRepository = BagRepositoryTestDouble
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