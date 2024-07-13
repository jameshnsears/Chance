package com.github.jameshnsears.chance.ui.tab.roll.compose


import android.app.Application
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.utility.UtilityDataHelper
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import io.mockk.mockk

@UtilityPreview
@Composable
fun TabRollPreview() {
    val repositorySettings = UtilityDataHelper().repositorySettings

    val repositoryBag = UtilityDataHelper().repositoryBag

    val repositoryRoll = UtilityDataHelper().repositoryRoll

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TabRollLayout(
                TabRollAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                ),
                ZoomAndroidViewModel(
                    mockk<Application>(),
                    repositoryBag,
                    repositoryRoll
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@UtilityPreview
@Composable
fun TabRollBottomSheetPreview() {
    val repositorySettings = UtilityDataHelper().repositorySettings

    val repositoryBag = UtilityDataHelper().repositoryBag

    val repositoryRoll = UtilityDataHelper().repositoryRoll

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TabRollBottomSheetLayout(
                TabRollAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                ),
                mockk<BottomSheetScaffoldState>()
            )
        }
    }
}
