package com.github.jameshnsears.chance.utility.feature

sealed class UtilityFeature {
    enum class Flag {
        NONE,                   // debug unit tests
        USE_PROTO_REPO          // debug instrumented tests / app
    }

    companion object {
        var enabled = setOf(
            Flag.NONE,
            Flag.USE_PROTO_REPO
        )

        fun isEnabled(flag: Flag = Flag.NONE): Boolean {
            return enabled.contains(flag)
        }
    }
}
