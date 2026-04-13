package com.github.jameshnsears.chance.ui.tab.bag

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModel


@Composable
fun Resize(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel,
) {
    val stateFlowResize =
        tabBagAndroidViewModel.stateFlowResize.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Slider(
            value = stateFlowResize.value.toFloat(),
            onValueChange = { newValue ->
                tabBagAndroidViewModel.resizeSettings(newValue.toInt())
                zoomBagAndroidViewModel.setResizeView(newValue.toInt())
            },
            valueRange = 1f..9f,
            steps = 7,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.testTag(BagTestTag.RESIZE)
        )
    }
}
