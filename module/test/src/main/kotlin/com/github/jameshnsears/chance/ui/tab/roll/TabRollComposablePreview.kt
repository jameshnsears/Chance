package com.github.jameshnsears.chance.ui.tab.roll

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.bag.BagSampleData
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview(heightDp = 800)
@Composable
fun TabRollComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            TabRollLayout(getTabRollViewModel())
        }
    }
}

@Preview(heightDp = 500)
@Composable
fun TabRollBottomSheetLayoutComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            TabRollBottomSheetLayout(getTabRollViewModel())
        }
    }
}

fun getTabRollViewModel(): TabRollViewModel {
    val settingsRepository = SettingsRepositoryTestDouble.getInstance()

    val bagRepository = BagRepositoryTestDouble.getInstance()
    bagRepository.store(BagSampleData.allDice)

    val rollRepository = RollRepositoryTestDouble.getInstance()
    rollRepository.store(RollSampleData.rollHistory)

    return TabRollViewModel(
        settingsRepository,
        bagRepository,
        rollRepository
    )
}