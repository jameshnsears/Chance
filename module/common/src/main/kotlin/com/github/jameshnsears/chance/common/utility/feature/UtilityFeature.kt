package com.github.jameshnsears.chance.common.utility.feature

sealed class UtilityFeature {
    enum class Flag {
        REPO_SQLDELIGHT_PROD,
        REPO_SQLDELIGHT_TEST_DOUBLE,
        REPO_SQLDELIGHT_EMPTY_AT_STARTUP,
        UI_SHOW_CRASHLYTICS_BUTTON,
        UI_SHOW_UUID,
    }

    companion object {
        var enabled = setOf(
            Flag.REPO_SQLDELIGHT_PROD,
//            Flag.REPO_SQLDELIGHT_EMPTY_AT_STARTUP,
//            Flag.REPO_SQLDELIGHT_TEST_DOUBLE,
//            Flag.UI_SHOW_UUID
        )

        fun isEnabled(flag: Flag = Flag.REPO_SQLDELIGHT_TEST_DOUBLE): Boolean {
            return enabled.contains(flag)
        }
    }
}
