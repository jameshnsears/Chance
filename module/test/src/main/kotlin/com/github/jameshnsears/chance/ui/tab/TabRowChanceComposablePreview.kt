package com.github.jameshnsears.chance.ui.tab

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.roll.repository.RollRepositoryMock
import com.github.jameshnsears.chance.data.roll.sample.RollSampleData
import com.github.jameshnsears.chance.ui.tab.bag.TabBagViewModel
import com.github.jameshnsears.chance.ui.tab.roll.TabRollViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview
@Composable
fun TabRowChanceComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val bagRepository = BagRepositoryMock
            bagRepository.store(BagDemoData.dice)

            val rollRepository = RollRepositoryMock
            rollRepository.store(RollSampleData.rollHistory_roll1Sequence1)

            TabRowChance(
                TabBagViewModel(bagRepository),
                TabRollViewModel(rollRepository)
            )
        }
    }
}