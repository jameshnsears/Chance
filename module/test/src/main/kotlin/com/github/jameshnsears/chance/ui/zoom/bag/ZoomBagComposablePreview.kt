package com.github.jameshnsears.chance.ui.zoom.bag

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview(heightDp = 800, widthDp = 360)
@Composable
fun ZoomBagComposablePreviewPortrait() {
    val bagRepository = BagRepositoryMock
    bagRepository.store(BagDemoData.dice)

    val viewModel = ZoomBagViewModel(bagRepository)

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ZoomBag(viewModel)
        }
    }
}

@Preview(heightDp = 1000, widthDp = 1000)
@Composable
fun ZoomBagComposablePreviewLandscape() {
    val bagRepository = BagRepositoryMock
    bagRepository.store(BagSampleData.allDice)

    val viewModel = ZoomBagViewModel(bagRepository)

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ZoomBag(viewModel)
        }
    }
}