package com.github.jameshnsears.chance.ui.zoom.setup.dice

import android.app.Application
import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Test

class ZoomDiceAndroidViewModelFactoryUnitTest {
    private val application: Application = mockk(relaxed = true)
    private val repositorySettings: RepositorySettingsInterface = mockk(relaxed = true)
    private val repositoryBag: RepositoryBagInterface = mockk(relaxed = true)
    private val repositoryRoll: RepositoryRollInterface = mockk(relaxed = true)

    private val factory = ZoomDiceAndroidViewModelFactory(
        application,
        repositorySettings,
        repositoryBag,
        repositoryRoll
    )

    @Test
    fun `create ZoomBagAndroidViewModel`() {
        val viewModel = factory.create(ZoomDiceAndroidViewModel::class.java)
        assertNotNull(viewModel)
    }

    @Test
    fun `create unknown ViewModel throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            factory.create(UnknownViewModel::class.java)
        }
    }

    private class UnknownViewModel : ViewModel()
}
