package com.github.jameshnsears.chance.ui.tab.bag

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.settings.repository.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview(heightDp = 500)
@Composable
fun TabBagComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {


            TabBagLayout(
                getTabBagViewModel()
            )
        }
    }
}


@Preview
@Composable
fun TabBagBottomSheetLayoutComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            TabBagBottomSheetLayout(getTabBagViewModel())
        }
    }
}

fun getTabBagViewModel() : TabBagViewModel {
    val settingsRepository = SettingsRepositoryTestDouble

    val bagRepository = BagRepositoryTestDouble
    bagRepository.store(BagDemoData.dice)

    return TabBagViewModel(settingsRepository, bagRepository)
}