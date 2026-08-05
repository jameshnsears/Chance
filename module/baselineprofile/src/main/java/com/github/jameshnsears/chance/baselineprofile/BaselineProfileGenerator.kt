package com.github.jameshnsears.chance.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collect(
            packageName = BenchmarkConfig.targetPackageName,

            // See: https://d.android.com/topic/performance/baselineprofiles/dex-layout-optimizations
            includeInStartupProfile = true
        ) {
            pressHome()

            startActivityAndWait()

            device.wait(Until.hasObject(By.text("Setup")), 1_000)
            device.findObject(By.text("Setup")).click()
            device.waitForIdle()

            val diceList = device.findObject(By.scrollable(true))
            diceList?.fling(Direction.DOWN)
            device.waitForIdle()

            device.findObject(By.text("2"))?.click()
            device.waitForIdle()

            /////////////

            device.findObject(By.text("Side"))?.click()
            device.waitForIdle()

            device.findObject(By.text("Rules"))?.click()
            device.waitForIdle()

            device.findObject(By.text("Save"))?.click()
            device.waitForIdle()

            /////////////

            device.findObject(By.text("Rolls")).click()
            device.waitForIdle()
        }
    }
}
