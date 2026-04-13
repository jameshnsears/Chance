package com.github.jameshnsears.chance.ui.tab.roll.selection

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.UtilityPreview
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.tab.roll.RollAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.RollAndroidViewModelFactory
import io.mockk.mockk

@SuppressLint("UnrememberedMutableState")
@UtilityPreview
@Composable
fun RollSelectionPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositoryFactory = RepositoryFactory()

    val rollAndroidViewModel: RollAndroidViewModel = viewModel(
        factory = RollAndroidViewModelFactory(
            mockk<Application>(),
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll
        )
    )

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            Row {
                RollSectionFilterChip(
                    rollAndroidViewModel, BagDataTestDouble().d2
                )

                RollSectionFilterChip(
                    rollAndroidViewModel, BagDataTestDouble().d4
                )

                RollSectionFilterChip(
                    rollAndroidViewModel, BagDataTestDouble().d4
                )

                RollSectionFilterChip(
                    rollAndroidViewModel, BagDataTestDouble().d12
                )
            }
        }
    }
}
