package com.github.jameshnsears.chance

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.security.SecureRandom

class ShortcutActivity : AppCompatActivity() {
    private val secureRandom = SecureRandom()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val thumbDown = secureRandom.nextInt(2) == 1

        setContent {
            YesNoDialog(
                isThumbDown = thumbDown,
                contentDescription = if (thumbDown) this.getString(R.string.app_shortcut_no) else this.getString(R.string.app_shortcut_yes)
            )
        }
    }
}
