package com.github.jameshnsears.chance.utility.feature

sealed class UtilityFeature {
    enum class Flag {
        NONE,
        USE_PROTO_REPO       // debug app from MainActivity only
    }

    companion object {
        private val enabled = setOf(
            Flag.NONE,
//            Flag.USE_PROTO_REPO
        )

        fun isEnabled(flag: Flag = Flag.NONE): Boolean {
            return enabled.contains(flag)
        }
    }
}
