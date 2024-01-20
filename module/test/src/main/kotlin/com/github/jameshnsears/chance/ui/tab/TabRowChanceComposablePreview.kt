package com.github.jameshnsears.chance.ui.tab

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview
@Composable
fun TabRowChanceComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val settingsRepository = SettingsRepositoryTestDouble.getInstance()

            val bagRepository = BagRepositoryTestDouble.getInstance()
            bagRepository.store(BagDemoSampleData.allDice)

            val rollRepository = RollRepositoryTestDouble.getInstance()
            rollRepository.store(RollSampleData.rollHistory)

            TabRowChance(
                settingsRepository,
                bagRepository,
                rollRepository
            )
        }
    }
}