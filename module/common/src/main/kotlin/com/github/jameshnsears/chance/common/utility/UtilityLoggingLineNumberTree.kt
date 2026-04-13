package com.github.jameshnsears.chance.common.utility

import timber.log.Timber

class UtilityLoggingLineNumberTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return String.format(
            "%s, %s",
            element.lineNumber,
            element.methodName,
        )
    }
}
