package com.github.jameshnsears.chance.ui.tab.rolls

import android.content.Context
import android.speech.tts.TextToSpeech
import timber.log.Timber
import java.util.Locale

class RollsScoreTtsPlayer(
    context: Context,
    textToSpeechFactory: (Context, TextToSpeech.OnInitListener) -> TextToSpeech = { ctx, listener ->
        TextToSpeech(ctx, listener)
    }
) {
    private var textToSpeech: TextToSpeech? = null
    var isInitialized = false
        private set

    init {
        try {
            textToSpeech = textToSpeechFactory(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech?.let { tts ->
                        val result = tts.setLanguage(Locale.getDefault())
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Timber.w("TTS: Language not supported")
                        } else {
                            isInitialized = true
                        }
                    }
                } else {
                    Timber.e("TTS: Initialization failed")
                }
            }
        } catch (e: NoClassDefFoundError) {
            Timber.w("TTS: TextToSpeech not available (likely in Compose Preview)")
        }
    }

    fun playScore(score: Int) {
        if (isInitialized) {
            textToSpeech?.speak(score.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            Timber.w("TTS: Not initialized yet")
        }
    }

    fun release() {
        textToSpeech?.let {
            it.stop()
            it.shutdown()
        }
    }
}
