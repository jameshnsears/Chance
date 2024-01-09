package com.github.jameshnsears.chance.ui.tab

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.roll.repository.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.roll.sample.RollSampleData
import com.github.jameshnsears.chance.data.settings.repository.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview
@Composable
fun TabRowChanceComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val settingsRepository = SettingsRepositoryTestDouble

            val bagRepository = BagRepositoryTestDouble
            bagRepository.store(BagDemoData.dice)

            val rollRepository = RollRepositoryTestDouble
            rollRepository.store(RollSampleData.rollHistory_roll1Sequence1)

            TabRowChance(
                settingsRepository,
                bagRepository,
                rollRepository
            )
        }
    }
}