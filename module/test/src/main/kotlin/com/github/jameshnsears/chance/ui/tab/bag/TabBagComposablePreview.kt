package com.github.jameshnsears.chance.ui.tab.bag


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

@Preview(heightDp = 500)
@Composable
fun TabBagComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TabBagLayout(
                getTabBagViewModel(),
            )
        }
    }
}


@Preview
@Composable
fun TabBagBottomSheetLayoutComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TabBagBottomSheetLayout(
                getTabBagViewModel(),
            )
        }
    }
}

fun getTabBagViewModel(): TabBagViewModel {
    val settingsRepository = SettingsRepositoryTestDouble.getInstance()

    val bagRepository = DiceBagRepositoryTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        bagRepository.store(BagDemoSampleData.allDice)
    }

    val rollRepository = RollRepositoryTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        rollRepository.store(RollSampleData.rollHistory)
    }

    return TabBagViewModel(
        settingsRepository,
        bagRepository,
        rollRepository,
    )
}
