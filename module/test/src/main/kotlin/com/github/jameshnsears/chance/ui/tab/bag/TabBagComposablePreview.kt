package com.github.jameshnsears.chance.ui.tab.bag

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview
@Composable
fun TabBagComposablePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val bagRepository = BagRepositoryMock
            bagRepository.store(BagDemoData.dice)

            TabBagLayout(TabBagViewModel(bagRepository))
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
            TabBagBottomSheetLayout()
        }
    }
}