package com.github.jameshnsears.chance.ui.dialog.bag.card

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ColourSampleCanvas(sideColour: String) {
    Canvas(
        modifier = Modifier
            .size(40.dp)
    ) {
        drawRect(
            color = Color(android.graphics.Color.parseColor("#${sideColour}")),
            topLeft = Offset(0f, 0f),
            size = size
        )
    }
}
