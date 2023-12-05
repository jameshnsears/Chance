package com.github.jameshnsears.chance.utils.logging

import timber.log.Timber

class LoggingLineNumberTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return String.format(
            "%s, %s",
            element.lineNumber,
            element.methodName
        )
    }
}