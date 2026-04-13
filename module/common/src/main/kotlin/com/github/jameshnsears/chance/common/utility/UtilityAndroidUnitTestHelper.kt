package com.github.jameshnsears.chance.common.utility

import android.app.Application
import android.content.Context
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

    protected fun application(): Application {
        val application = mockk<Application>()
        every { application.getString(any()) } returns ""
        every { application.baseContext } returns mockk<Context>()
        return application
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
