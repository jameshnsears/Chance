package com.github.jameshnsears.chance.ui.tab.setup.dice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class DiceAndroidViewModelFactoryUnitTest {
    private val application = mockk<Application>(relaxed = true)
    private val repositorySettings = mockk<RepositorySettingsInterface>(relaxed = true)
    private val repositoryBag = mockk<RepositoryBagInterface>(relaxed = true)
    private val repositoryRoll = mockk<RepositoryRollInterface>(relaxed = true)
    private val repositoryGroup = mockk<RepositoryGroupInterface>(relaxed = true)
    private val resizeInitialValue = 2f

    private val factory = DiceAndroidViewModelFactory(
        application,
        repositorySettings,
        repositoryBag,
        repositoryRoll,
        repositoryGroup,
        resizeInitialValue
    )

    @Test
    fun createTabBagAndroidViewModel() {
        val viewModel = factory.create(DiceAndroidViewModel::class.java)
        assertNotNull(viewModel)
    }

    @Test
    fun createTabBagAndroidViewModelAsAndroidViewModel() {
        val viewModel = factory.create(AndroidViewModel::class.java)
        assertNotNull(viewModel)
        assertTrue(viewModel is DiceAndroidViewModel)
    }

    @Test
    fun createUnknownViewModel() {
        class UnknownViewModel : ViewModel()
        assertThrows(IllegalArgumentException::class.java) {
            factory.create(UnknownViewModel::class.java)
        }
    }
}
