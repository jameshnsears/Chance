package com.github.jameshnsears.chance.baselineprofile

import androidx.test.platform.app.InstrumentationRegistry

object BenchmarkConfig {
    private const val DEFAULT_PACKAGE_NAME = "com.github.jameshnsears.chance"

    val targetPackageName: String
        get() = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: DEFAULT_PACKAGE_NAME
}
