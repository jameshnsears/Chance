package com.github.jameshnsears.chance.utility.feature

sealed class UtilityFeature {
    enum class Flag {
        NONE,                   // debug unit tests
        USE_PROTO_REPO,         // production data, comment out to use test data
        CRASHLYTICS,            // display only a single CRASHLYTICS button in MainActivity
        SHOW_EPOCH_UUID         // show Dice.epoch; Dice.uuid; Side.uuid
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
