package com.github.jameshnsears.chance.common.utility

import android.app.Application
import android.content.Context
import android.os.Vibrator
import android.os.VibratorManager
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule

open class UtilityAndroidUnitTestHelper {
    @get:Rule
    val utilityRuleMainDispatcher = UtilityRuleMainDispatcher()

    init {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )
    }

    private var applicationMock: Application? = null

    @android.annotation.SuppressLint("NewApi")
    protected fun application(): Application {
        if (applicationMock == null) {
            applicationMock = mockk<Application>()
            every { applicationMock!!.getString(any()) } returns ""
            every { applicationMock!!.baseContext } returns mockk<Context>()
            every { applicationMock!!.getSystemService(Vibrator::class.java) } returns mockk<Vibrator>(relaxed = true)
            every { applicationMock!!.getSystemService(VibratorManager::class.java) } returns mockk<VibratorManager>(relaxed = true)
        }
        return applicationMock!!
    }

    protected fun resourceAsByteArray(resource: String): ByteArray {
        val url = this::class.java.getResource(resource)
        return url!!.readBytes()
    }

    protected fun resourceAsString(resource: String): String {
        val url = this::class.java.getResource(resource)
        return url!!.readText()
    }
}
