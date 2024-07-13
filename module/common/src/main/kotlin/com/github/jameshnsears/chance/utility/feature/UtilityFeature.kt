package com.github.jameshnsears.chance.utility.feature

sealed class UtilityFeature {
    enum class Flag {
        NONE,
        USE_PROD_REPO
    }

    companion object {
        private val enabled = setOf(
            Flag.NONE,
//            Flag.USE_PROD_REPO
        )

        fun isEnabled(flag: Flag): Boolean {
            return enabled.contains(flag)
        }
    }
}