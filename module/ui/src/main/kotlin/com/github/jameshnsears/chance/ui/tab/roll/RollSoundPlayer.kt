package com.github.jameshnsears.chance.ui.tab.roll

import android.content.Context
import android.media.MediaPlayer
import com.github.jameshnsears.chance.ui.R
import timber.log.Timber

class RollSoundPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.roll)
        }

        mediaPlayer?.let { mp ->
            if (mp.isPlaying) {
                try {
                    mp.stop()
                } catch (e: IllegalStateException) {
                    Timber.w(e, "Failed to top roll sound")
                }
                mp.release()
                mediaPlayer = MediaPlayer.create(context, R.raw.roll)
            }

            try {
                mediaPlayer?.start()
            } catch (e: IllegalStateException) {
                Timber.w(e, "Failed to start roll sound")
            }
        }
    }

    fun release() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    try {
                        it.stop()
                    } catch (e: IllegalStateException) {
                        Timber.w(e, "Failed to stop mediaPlayer")
                    }
                }
                it.release()
            }
        } catch (e: Exception) {
            Timber.w(e, "Failed to release mediaPlayer")
        } finally {
            mediaPlayer = null
        }
    }
}
