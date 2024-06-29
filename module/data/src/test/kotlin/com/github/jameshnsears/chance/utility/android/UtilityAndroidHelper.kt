package com.github.jameshnsears.chance.utility.android

import com.github.jameshnsears.chance.utility.rule.UtilityRuleMainDispatcher
import org.junit.Rule

open class UtilityAndroidHelper {
    @get:Rule
    val utilityRuleMainDispatcher = UtilityRuleMainDispatcher()

    protected fun getResourceAsByteArray(resource: String): ByteArray {
        val url = this::class.java.getResource(resource)
        return url!!.readBytes()
    }

    protected fun getResourceAsString(resource: String): String {
        val url = this::class.java.getResource(resource)
        return url!!.readText()
    }
}