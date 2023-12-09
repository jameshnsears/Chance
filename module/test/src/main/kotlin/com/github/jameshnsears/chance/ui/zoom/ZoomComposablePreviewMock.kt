package com.github.jameshnsears.chance.ui.zoom

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview(heightDp = 1300, widthDp = 360)
@Composable
fun ZoomComposablePreviewMockPortrait() {
    val viewModel = ZoomViewModel(BagRepositoryMock)

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ZoomColumn(viewModel)
        }
    }
}
