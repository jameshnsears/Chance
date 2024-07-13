package com.github.jameshnsears.chance.ui.dialog.bag

import androidx.compose.ui.test.junit4.createComposeRule
import com.github.jameshnsears.chance.data.utility.UtilityDataHelper
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import org.junit.Rule

open class DialogBagInstrumentedHelper : UtilityLoggingInstrumentedHelper() {
    protected val repositoryBag = UtilityDataHelper().repositoryBag

    @get:Rule
    val composeRule = createComposeRule()
}
