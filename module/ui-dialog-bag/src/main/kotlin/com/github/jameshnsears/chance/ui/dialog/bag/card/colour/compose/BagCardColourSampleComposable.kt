package com.github.jameshnsears.chance.ui.dialog.bag.card.colour.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BagCardColourSample(sideColour: String) {
    Canvas(
        modifier = Modifier
            .size(40.dp),
    ) {
        drawCircle(
            color = Color(android.graphics.Color.parseColor("#${sideColour}")),
            radius = 24.dp.toPx(),
            center = Offset(size.width / 2f, size.height / 2f)
        )
    }
}
