package com.github.jameshnsears.chance.ui.zoom.roll

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme


@Preview(heightDp = 750, widthDp = 1000)
@Composable
fun ZoomRollComposablePreview() {
    val settingsRepository = SettingsRepositoryTestDouble.getInstance()

    val rollRepository = RollRepositoryTestDouble.getInstance()
    rollRepository.store(RollSampleData.rollHistory)

    val zoomRollViewModel = ZoomRollViewModel(settingsRepository, rollRepository)

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ZoomRoll(zoomRollViewModel)
        }
    }
}
