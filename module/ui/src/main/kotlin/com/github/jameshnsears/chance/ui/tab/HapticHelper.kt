package com.github.jameshnsears.chance.ui.tab

import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class HapticHelper(context: Context) {
    val vibrator: Vibrator? = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(VibratorManager::class.java)
            vibratorManager?.defaultVibrator
        } else {
            context.getSystemService(Vibrator::class.java)
        }
    } catch (e: Exception) {
        Timber.w(e, "Vibrator not available")
        null
    }

    private fun vibrate(effect: VibrationEffect) {
        val v = vibrator ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val attrs = VibrationAttributes.Builder()
                .setUsage(VibrationAttributes.USAGE_TOUCH)
                .build()
            v.vibrate(effect, attrs)
        } else {
            val attrs = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .build()
            @Suppress("DEPRECATION")
            v.vibrate(effect, attrs)
        }
    }

    suspend fun playRollHaptic() = withContext(Dispatchers.Default) {
        val v = vibrator ?: return@withContext
        if (!v.hasVibrator()) return@withContext

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            v.areAllPrimitivesSupported(
                VibrationEffect.Composition.PRIMITIVE_THUD,
                VibrationEffect.Composition.PRIMITIVE_CLICK
            )
        ) {
            vibrate(
                VibrationEffect.startComposition()
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, 1.0f)
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_CLICK, 1.0f)
                    .compose()
            )
        } else {
            vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    suspend fun playUndoHaptic() = withContext(Dispatchers.Default) {
        val v = vibrator ?: return@withContext
        if (!v.hasVibrator()) return@withContext

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            v.areAllPrimitivesSupported(VibrationEffect.Composition.PRIMITIVE_CLICK)
        ) {
            vibrate(
                VibrationEffect.startComposition()
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, 1.0f)
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, 1.0f, 20)
                    .compose()
            )
        } else {
            vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    suspend fun playUndoAllHaptic() = withContext(Dispatchers.Default) {
        val v = vibrator ?: return@withContext
        if (!v.hasVibrator()) return@withContext

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            v.areAllPrimitivesSupported(
                VibrationEffect.Composition.PRIMITIVE_THUD,
                VibrationEffect.Composition.PRIMITIVE_CLICK
            )
        ) {
            vibrate(
                VibrationEffect.startComposition()
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, 1.0f)
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, 1.0f, 20)
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, 1.0f, 40)
                    .compose()
            )
        } else {
            vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
