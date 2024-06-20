package com.github.jameshnsears.chance.utility.android

import android.app.Application
import android.content.Context
import com.github.jameshnsears.chance.utility.rule.UtilityRuleMainDispatcher
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule

open class UtilityAndroidHelper {
    @get:Rule
    val utilityRuleMainDispatcher = UtilityRuleMainDispatcher()

    protected fun getApplication(): Application {
        val application = mockk<Application>()
        every { application.getString(any()) } returns ""
        every { application.baseContext } returns mockk<Context>()
        return application
    }

    protected fun getResourceAsByteArray(resource: String): ByteArray {
        val url = this::class.java.getResource(resource)
        return url!!.readBytes()
    }

    protected fun getResourceAsString(resource: String): String {
        val url = this::class.java.getResource(resource)
        return url!!.readText()
    }
}