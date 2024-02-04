package com.github.jameshnsears.chance.ui.zoom.bag


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.BagSampleData
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@Preview(heightDp = 400, widthDp = 360)
@Composable
fun ZoomBagComposablePreviewPortrait() {
    val bagRepositoryTestDouble = DiceBagRepositoryTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        bagRepositoryTestDouble.store(BagDemoSampleData.allDice)
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            ZoomBag(
                ZoomBagViewModel(
                    SettingsRepositoryTestDouble.getInstance(),
                    bagRepositoryTestDouble,
                ),
            )
        }
    }
}

@Preview(heightDp = 900, widthDp = 1000)
@Composable
fun ZoomBagComposablePreviewLandscape() {
    val bagRepositoryTestDouble = DiceBagRepositoryTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        bagRepositoryTestDouble.store(BagSampleData.allDice)
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            ZoomBag(
                ZoomBagViewModel(
                    SettingsRepositoryTestDouble.getInstance(),
                    bagRepositoryTestDouble,
                ),
            )
        }
    }
}
