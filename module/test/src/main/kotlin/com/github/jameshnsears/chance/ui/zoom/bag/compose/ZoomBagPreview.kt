package com.github.jameshnsears.chance.ui.zoom.bag.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.compose.getViewModels
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview

@UtilityPreview
@Composable
fun ZoomBagPreview() {
    val (tabBagViewModel, zoomViewModel) = getViewModels()

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            ZoomBag(zoomViewModel)
        }
    }
}
