package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

open class DialogBagInstrumentedHelper : UtilityLoggingInstrumentedHelper() {
    protected val repositoryBag = RepositoryBagTestDouble.getInstance()

    init {
        runBlocking(Dispatchers.Main) {
            repositoryBag.store(
                mutableListOf(
                    SampleBag.d2,
                ),
            )
        }
    }
}
