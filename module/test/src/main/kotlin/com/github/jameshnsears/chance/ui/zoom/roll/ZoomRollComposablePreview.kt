package com.github.jameshnsears.chance.ui.zoom.roll

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


@Preview(heightDp = 1100, widthDp = 900)
@Composable
fun ZoomRollComposablePreview() {
    val bagRepositoryTestDouble = DiceBagRepositoryTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        bagRepositoryTestDouble.store(BagDemoSampleData.allDice)
    }

    val rollRepositoryTestDouble = RollRepositoryTestDouble.getInstance()
    rollRepositoryTestDouble.store(RollSampleData.rollHistory)

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            ZoomRoll(
                ZoomRollViewModel(
                    SettingsRepositoryTestDouble.getInstance(),
                    bagRepositoryTestDouble,
                    rollRepositoryTestDouble,
                ),
            )
        }
    }
}
