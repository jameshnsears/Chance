package com.github.jameshnsears.chance.ui.tab

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.bag.demo.BagDemo
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.ui.tab.bag.TabBagViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview
@Composable
fun TabRowChancePreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val bagRepository = BagRepositoryMock
            bagRepository.store(BagDemo.dice)

            TabRowChance(
                TabBagViewModel(bagRepository)
            )
        }
    }
}