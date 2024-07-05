package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.repository.bag.testdouble.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

open class DialogBagInstrumentedHelper : UtilityLoggingInstrumentedHelper() {
    protected val repositoryBag = RepositoryBagTestDouble.getInstance()

    init {
        val sampleBagTestData = SampleBagTestData()

        runBlocking(Dispatchers.Main) {
            repositoryBag.store(
                mutableListOf(
                    sampleBagTestData.d2,
                ),
            )
        }
    }
}
