package com.github.jameshnsears.chance.utility.feature

sealed class UtilityFeature {
    enum class Flag {
        REPO_PROTOCOL_BUFFER,
        REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        UI_SHOW_CRASHLYTICS_BUTTON,
        UI_SHOW_EPOCH_UUID
    }

    companion object {
        var enabled = setOf(
            Flag.REPO_PROTOCOL_BUFFER,
        )

        fun isEnabled(flag: Flag = Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE): Boolean {
            return enabled.contains(flag)
        }
    }
}
