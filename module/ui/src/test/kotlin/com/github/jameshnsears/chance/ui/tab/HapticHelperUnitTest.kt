package com.github.jameshnsears.chance.ui.tab

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class HapticHelperUnitTest : UtilityAndroidUnitTestHelper() {
    private lateinit var hapticHelper: HapticHelper
    private lateinit var mockVibrator: Vibrator
    private lateinit var mockVibratorManager: VibratorManager
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockVibrator = mockk(relaxed = true)
        mockVibratorManager = mockk(relaxed = true)
        mockContext = mockk()

        every { mockContext.getSystemService(Vibrator::class.java) } returns mockVibrator
        every { mockContext.getSystemService(VibratorManager::class.java) } returns mockVibratorManager
        every { mockVibratorManager.defaultVibrator } returns mockVibrator
        every { mockVibrator.hasVibrator() } returns true

        mockkStatic(VibrationEffect::class)
        every { VibrationEffect.createOneShot(any(), any()) } returns mockk()

        hapticHelper = spyk(HapticHelper(mockContext), recordPrivateCalls = true)
        every { hapticHelper["vibrate"](any<VibrationEffect>()) } returns Unit
    }

    @After
    fun tearDown() {
        unmockkStatic(VibrationEffect::class)
    }

    @Test
    fun playRollHaptic() = runTest {
        hapticHelper.playRollHaptic()
        verify { hapticHelper["vibrate"](any<VibrationEffect>()) }
    }

    @Test
    fun playUndoHaptic() = runTest {
        hapticHelper.playUndoHaptic()
        verify { hapticHelper["vibrate"](any<VibrationEffect>()) }
    }

    @Test
    fun playUndoAllHaptic() = runTest {
        hapticHelper.playUndoAllHaptic()
        verify { hapticHelper["vibrate"](any<VibrationEffect>()) }
    }

    @Test
    fun noVibrator() = runTest {
        every { mockVibrator.hasVibrator() } returns false
        hapticHelper.playRollHaptic()
        verify(exactly = 0) { hapticHelper["vibrate"](any<VibrationEffect>()) }
    }
}
