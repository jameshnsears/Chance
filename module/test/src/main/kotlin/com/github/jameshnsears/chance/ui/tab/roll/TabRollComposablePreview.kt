package com.github.jameshnsears.chance.ui.tab.roll

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.roll.repository.RollRepositoryMock
import com.github.jameshnsears.chance.data.roll.sample.RollSampleData
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview
@Composable
fun TabRollComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val rollRepository = RollRepositoryMock
            rollRepository.store(RollSampleData.rollHistory_roll1Sequence1)

            val viewModel = TabRollViewModel(rollRepository)

            TabRollLayout(viewModel)
        }
    }
}

@Preview
@Composable
fun TabRollBottomSheetLayoutComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            TabRollBottomSheetLayout()
        }
    }
}