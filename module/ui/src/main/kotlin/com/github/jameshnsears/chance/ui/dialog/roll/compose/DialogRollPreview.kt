package com.github.jameshnsears.chance.ui.dialog.roll.compose

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.repository.bag.testdouble.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.testdouble.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.testdouble.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.utility.UtilityDataHelper
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import io.mockk.mockk

@SuppressLint("UnrememberedMutableState")
@UtilityPreview
@Composable
fun DialogRollPreview() {
    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            DialogRollLayout(
                TabRollAndroidViewModel(
                    mockk<Application>(),
                    RepositorySettingsTestDouble.getInstance(),
                    RepositoryBagTestDouble.getInstance(UtilityDataHelper().bagDataTestDouble.allDice),
                    RepositoryRollTestDouble.getInstance(UtilityDataHelper().rollHistoryDataTestDouble)
                )
            )
        }
    }
}
