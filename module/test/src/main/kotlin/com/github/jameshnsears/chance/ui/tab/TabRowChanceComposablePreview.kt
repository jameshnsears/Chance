package com.github.jameshnsears.chance.ui.tab

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

@Preview
@Composable
fun TabRowChanceComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            val settingsRepository = SettingsRepositoryTestDouble.getInstance()

            val bagRepository = DiceBagRepositoryTestDouble.getInstance()
            runBlocking(Dispatchers.Main) {
                bagRepository.store(BagDemoSampleData.allDice)
            }

            val rollRepository = RollRepositoryTestDouble.getInstance()
            runBlocking(Dispatchers.Main) {
                rollRepository.store(RollSampleData.rollHistory)
            }

            TabRowChance(
                settingsRepository,
                bagRepository,
                rollRepository,
            )
        }
    }
}
