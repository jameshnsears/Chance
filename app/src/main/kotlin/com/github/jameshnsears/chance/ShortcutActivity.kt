package com.github.jameshnsears.chance

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import java.security.SecureRandom

class ShortcutActivity : AppCompatActivity() {
    private val secureRandom: SecureRandom = SecureRandom.getInstance("SHA1PRNG")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YesNo(secureRandom)
        }
    }
}

@Composable
fun YesNo(secureRandom: SecureRandom) {
    val context = LocalContext.current as ShortcutActivity

    val painterResource = when (secureRandom.nextInt(2)) {
        1 -> painterResource(id = R.drawable.outline_thumb_down_24)
        else -> painterResource(id = R.drawable.outline_thumb_up_24)
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
        ) {
            Image(
                painter = painterResource,
                contentDescription = "",
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
