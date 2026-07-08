package com.github.jameshnsears.chance.ui.tab.rolls

import android.content.Context
import android.media.MediaPlayer
import com.github.jameshnsears.chance.ui.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class RollsSoundPlayerTest {
    private lateinit var context: Context
    private lateinit var rollsSoundPlayer: RollsSoundPlayer

    @Before
    fun setUp() {
        context = mockk()
        mockkStatic(MediaPlayer::class)
        rollsSoundPlayer = RollsSoundPlayer(context)
    }

    @After
    fun tearDown() {
        unmockkStatic(MediaPlayer::class)
    }

    @Test
    fun `play creates and starts mediaPlayer`() {
        val mediaPlayer = mockk<MediaPlayer>(relaxed = true)
        every { MediaPlayer.create(context, R.raw.roll) } returns mediaPlayer

        rollsSoundPlayer.play()

        verify { MediaPlayer.create(context, R.raw.roll) }
        verify { mediaPlayer.start() }
    }

    @Test
    fun `play stops and releases if already playing`() {
        val mediaPlayer1 = mockk<MediaPlayer>(relaxed = true)
        val mediaPlayer2 = mockk<MediaPlayer>(relaxed = true)

        every { MediaPlayer.create(context, R.raw.roll) } returns mediaPlayer1 andThen mediaPlayer2
        every { mediaPlayer1.isPlaying } returnsMany listOf(false, true)

        rollsSoundPlayer.play() // mediaPlayer1 created. isPlaying is false. start() called on mediaPlayer1.
        rollsSoundPlayer.play() // mediaPlayer1 isPlaying is true. stop()/release() called on mediaPlayer1. mediaPlayer2 created. start() called on mediaPlayer2.

        verify { mediaPlayer1.start() }
        verify { mediaPlayer1.stop() }
        verify { mediaPlayer1.release() }
        verify { mediaPlayer2.start() }
    }

    @Test
    fun `play does nothing if MediaPlayer create returns null`() {
        every { MediaPlayer.create(context, R.raw.roll) } returns null

        rollsSoundPlayer.play()

        verify { MediaPlayer.create(context, R.raw.roll) }
    }

    @Test
    fun `release stops and releases mediaPlayer`() {
        val mediaPlayer = mockk<MediaPlayer>(relaxed = true)
        every { MediaPlayer.create(context, R.raw.roll) } returns mediaPlayer
        every { mediaPlayer.isPlaying } returns true

        rollsSoundPlayer.play()
        rollsSoundPlayer.release()

        verify { mediaPlayer.stop() }
        verify { mediaPlayer.release() }
    }

    @Test
    fun `release handles exceptions gracefully`() {
        val mediaPlayer = mockk<MediaPlayer>(relaxed = true)
        every { MediaPlayer.create(context, R.raw.roll) } returns mediaPlayer
        every { mediaPlayer.isPlaying } returns true
        every { mediaPlayer.stop() } throws IllegalStateException()

        rollsSoundPlayer.play()
        rollsSoundPlayer.release()

        verify { mediaPlayer.stop() }
        verify { mediaPlayer.release() }
    }
}
