package com.github.jameshnsears.chance.ui.tab.roll.selection.composable

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.utility.feature.UtilityFeature.Flag
import io.mockk.mockk

@SuppressLint("UnrememberedMutableState")
@UtilityPreview
@Composable
fun RollSelectionPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositoryFactory = RepositoryFactory()

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            Row {
                RollSectionFilterChip(
                    TabRollAndroidViewModel(
                        mockk<Application>(),
                        repositoryFactory.repositorySettings,
                        repositoryFactory.repositoryBag,
                        repositoryFactory.repositoryRoll
                    ), BagDataTestDouble().d2
                )

                RollSectionFilterChip(
                    TabRollAndroidViewModel(
                        mockk<Application>(),
                        repositoryFactory.repositorySettings,
                        repositoryFactory.repositoryBag,
                        repositoryFactory.repositoryRoll
                    ), BagDataTestDouble().d4
                )

                RollSectionFilterChip(
                    TabRollAndroidViewModel(
                        mockk<Application>(),
                        repositoryFactory.repositorySettings,
                        repositoryFactory.repositoryBag,
                        repositoryFactory.repositoryRoll
                    ), BagDataTestDouble().d4
                )

                RollSectionFilterChip(
                    TabRollAndroidViewModel(
                        mockk<Application>(),
                        repositoryFactory.repositorySettings,
                        repositoryFactory.repositoryBag,
                        repositoryFactory.repositoryRoll
                    ), BagDataTestDouble().d12
                )
            }
        }
    }
}
