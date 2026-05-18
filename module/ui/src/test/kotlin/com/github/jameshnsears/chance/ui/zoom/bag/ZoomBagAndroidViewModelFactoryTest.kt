package com.github.jameshnsears.chance.ui.zoom.bag

import android.app.Application
import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Test

class ZoomBagAndroidViewModelFactoryTest {
    private val application: Application = mockk(relaxed = true)
    private val repositorySettings: RepositorySettingsInterface = mockk()
    private val repositoryBag: RepositoryBagInterface = mockk()
    private val repositoryRoll: RepositoryRollInterface = mockk()

    private val factory = ZoomBagAndroidViewModelFactory(
        application,
        repositorySettings,
        repositoryBag,
        repositoryRoll
    )

    @Test
    fun `create ZoomBagAndroidViewModel`() {
        val viewModel = factory.create(ZoomBagAndroidViewModel::class.java)
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
