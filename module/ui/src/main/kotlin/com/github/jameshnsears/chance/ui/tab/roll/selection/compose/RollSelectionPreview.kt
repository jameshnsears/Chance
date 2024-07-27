package com.github.jameshnsears.chance.ui.tab.roll.selection.compose

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.repository.bag.testdouble.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.testdouble.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.testdouble.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import io.mockk.mockk

@SuppressLint("UnrememberedMutableState")
@UtilityPreview
@Composable
fun RollSelectionPreview() {
    UtilityFeature.enabled = setOf(
        UtilityFeature.Flag.NONE,
    )

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            Row {
                RollSectionFilterChip(
                    TabRollAndroidViewModel(
                        mockk<Application>(),
                        RepositorySettingsTestDouble.getInstance(),
                        RepositoryBagTestDouble.getInstance(RepositoryFactory().bagDataTestDouble.allDice),
                        RepositoryRollTestDouble.getInstance(RepositoryFactory().rollHistoryDataTestDouble)
                    ), BagDataTestDouble().d2
                )

                RollSectionFilterChip(
                    TabRollAndroidViewModel(
                        mockk<Application>(),
                        RepositorySettingsTestDouble.getInstance(),
                        RepositoryBagTestDouble.getInstance(RepositoryFactory().bagDataTestDouble.allDice),
                        RepositoryRollTestDouble.getInstance(RepositoryFactory().rollHistoryDataTestDouble)
                    ), BagDataTestDouble().d4
                )

                RollSectionFilterChip(
                    TabRollAndroidViewModel(
                        mockk<Application>(),
                        RepositorySettingsTestDouble.getInstance(),
                        RepositoryBagTestDouble.getInstance(RepositoryFactory().bagDataTestDouble.allDice),
                        RepositoryRollTestDouble.getInstance(RepositoryFactory().rollHistoryDataTestDouble)
                    ), BagDataTestDouble().d4
                )

                RollSectionFilterChip(
                    TabRollAndroidViewModel(
                        mockk<Application>(),
                        RepositorySettingsTestDouble.getInstance(),
                        RepositoryBagTestDouble.getInstance(RepositoryFactory().bagDataTestDouble.allDice),
                        RepositoryRollTestDouble.getInstance(RepositoryFactory().rollHistoryDataTestDouble)
                    ), BagDataTestDouble().d12
                )
            }
        }
    }
}
