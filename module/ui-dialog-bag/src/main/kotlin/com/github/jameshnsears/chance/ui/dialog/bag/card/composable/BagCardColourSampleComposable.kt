package com.github.jameshnsears.chance.ui.dialog.bag.card.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BagCardColourSample(sideColour: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(1.dp, Color.Black, CircleShape)
    ) {
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
}
