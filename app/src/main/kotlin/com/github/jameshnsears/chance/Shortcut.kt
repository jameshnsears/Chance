package com.github.jameshnsears.chance

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat

@Composable
fun YesNoDialog(
    isThumbDown: Boolean,
    contentDescription: String
) {
    val context = LocalActivity.current as ShortcutActivity

    val painterResource = when (isThumbDown) {
        true -> painterResource(id = R.drawable.thumb_down)
        else -> painterResource(id = R.drawable.thumb_up)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(
                onClick = {
                    ActivityCompat.finishAffinity(context)
                },
            ),
        contentAlignment = Alignment.Center
    ) {
        Dialog(
            onDismissRequest = {
                ActivityCompat.finishAffinity(context)
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Image(
                painter = painterResource,
                contentDescription = contentDescription,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(256.dp)
                    .clickable(
                        onClick = {
                            ActivityCompat.finishAffinity(context)
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
            )
        }
    }
}
