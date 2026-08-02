package com.github.jameshnsears.chance.ui.tab.rolls

import android.content.Context
import android.speech.tts.TextToSpeech
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Locale

class RollsScoreTtsPlayerTest {
    private lateinit var context: Context
    private lateinit var ttsMock: TextToSpeech
    private val listenerSlot = slot<TextToSpeech.OnInitListener>()

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        ttsMock = mockk(relaxed = true)
    }

    private fun createPlayer(): RollsScoreTtsPlayer {
        return RollsScoreTtsPlayer(context) { _, listener ->
            listenerSlot.captured = listener
            ttsMock
        }
    }

    @Test
    fun `init successfully sets isInitialized to true`() {
        val player = createPlayer()
        assertFalse(player.isInitialized)

        every { ttsMock.setLanguage(any()) } returns TextToSpeech.LANG_AVAILABLE

        listenerSlot.captured.onInit(TextToSpeech.SUCCESS)

        assertTrue(player.isInitialized)
        verify { ttsMock.setLanguage(Locale.getDefault()) }
    }

    @Test
    fun `init failure does not set isInitialized`() {
        val player = createPlayer()
        assertFalse(player.isInitialized)

        listenerSlot.captured.onInit(TextToSpeech.ERROR)

        assertFalse(player.isInitialized)
        verify(exactly = 0) { ttsMock.setLanguage(any()) }
    }

    @Test
    fun `init with unsupported language does not set isInitialized`() {
        val player = createPlayer()
        assertFalse(player.isInitialized)

        every { ttsMock.setLanguage(any()) } returns TextToSpeech.LANG_NOT_SUPPORTED

        listenerSlot.captured.onInit(TextToSpeech.SUCCESS)

        assertFalse(player.isInitialized)
        verify { ttsMock.setLanguage(Locale.getDefault()) }
    }

    @Test
    fun `playScore calls speak when initialized`() {
        val player = createPlayer()
        every { ttsMock.setLanguage(any()) } returns TextToSpeech.LANG_AVAILABLE
        listenerSlot.captured.onInit(TextToSpeech.SUCCESS)

        player.playScore(42)

        verify { ttsMock.speak("42", TextToSpeech.QUEUE_FLUSH, null, null) }
    }

    @Test
    fun `playScore does nothing when not initialized`() {
        val player = createPlayer()
        // listener not called

        player.playScore(42)

        verify(exactly = 0) { ttsMock.speak(any(), any(), any(), any()) }
    }

    @Test
    fun `release stops and shuts down tts`() {
        val player = createPlayer()

        player.release()

        verify { ttsMock.stop() }
        verify { ttsMock.shutdown() }
        confirmVerified(ttsMock)
    }
}
