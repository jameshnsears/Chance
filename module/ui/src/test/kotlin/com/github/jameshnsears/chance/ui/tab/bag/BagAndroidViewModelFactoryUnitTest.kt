package com.github.jameshnsears.chance.ui.tab.bag

import android.app.Application
import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class BagAndroidViewModelFactoryUnitTest {
    private val application = mockk<Application>(relaxed = true)
    private val repositorySettings = mockk<RepositorySettingsInterface>(relaxed = true)
    private val repositoryBag = mockk<RepositoryBagInterface>(relaxed = true)
    private val repositoryRoll = mockk<RepositoryRollInterface>(relaxed = true)
    private val resizeInitialValue = 2

    private val factory = BagAndroidViewModelFactory(
        application,
        repositorySettings,
        repositoryBag,
        repositoryRoll,
        resizeInitialValue
    )

    @Test
    fun createTabBagAndroidViewModel() {
        val viewModel = factory.create(TabBagAndroidViewModel::class.java)
        assertNotNull(viewModel)
    }

    @Test
    fun createTabBagAndroidViewModelAsAndroidViewModel() {
        val viewModel = factory.create(androidx.lifecycle.AndroidViewModel::class.java)
        assertNotNull(viewModel)
        assertTrue(viewModel is TabBagAndroidViewModel)
    }

    @Test
    fun createUnknownViewModel() {
        class UnknownViewModel : ViewModel()
        assertThrows(IllegalArgumentException::class.java) {
            factory.create(UnknownViewModel::class.java)
        }
    }
}
