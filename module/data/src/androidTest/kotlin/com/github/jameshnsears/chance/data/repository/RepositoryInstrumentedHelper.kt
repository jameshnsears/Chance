package com.github.jameshnsears.chance.data.repository

import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper

open class RepositoryInstrumentedHelper : UtilityLoggingInstrumentedHelper() {
    init {
        UtilityFeature.enabled = setOf(
            Flag.NONE,
            Flag.USE_PROTO_REPO
        )
    }
}
