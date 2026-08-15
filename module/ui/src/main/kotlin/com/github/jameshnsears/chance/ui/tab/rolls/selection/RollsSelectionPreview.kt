package com.github.jameshnsears.chance.ui.tab.rolls.selection

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.rolls.RollsCoreHelper
import com.github.jameshnsears.chance.ui.tab.rolls.RollsSelectionHelper

@SuppressLint("UnrememberedMutableState", "ViewModelConstructorInComposable")
@Preview
@Composable
fun RollSelectionPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_SQLDELIGHT_TEST_DOUBLE,
    )

    val repositoryFactory = RepositoryFactory()

    val context = LocalContext.current
    val application = object : Application() {
        override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
            return context.getSharedPreferences(name, mode)
        }
    }

    val rollsAndroidViewModel = RollsAndroidViewModel(
        application,
        repositoryFactory.repositorySettings,
        repositoryFactory.repositoryBag,
        repositoryFactory.repositoryRoll,
        repositoryFactory.repositoryGroup,
        repositoryFactory.repositoryLocked,
        RollsSelectionHelper(
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryGroup
        ),
        RollsCoreHelper(repositoryFactory.repositoryRoll)
    )

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Row {
                RollSelectionFilterChip(
                    rollsAndroidViewModel, BagDataTestDouble().d2, true, true
                )

                RollSelectionFilterChip(
                    rollsAndroidViewModel, BagDataTestDouble().d4, true, true
                )

                RollSelectionFilterChip(
                    rollsAndroidViewModel, BagDataTestDouble().d4, true, true
                )

                RollSelectionFilterChip(
                    rollsAndroidViewModel, BagDataTestDouble().d12, true, true
                )
            }
        }
    }
}
