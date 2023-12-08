package com.github.jameshnsears.chance.ui.dialog.dice

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import com.github.jameshnsears.chance.ui.dialog.dice.slider.DiceSliderPenaltyBonusMock
import com.github.jameshnsears.chance.ui.dialog.dice.slider.DiceSliderSidesMock
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview(heightDp = 600, widthDp = 360)
@Composable
fun DialogDiceComposablePreviewMockPortrait() {
    DialogDiceComposablePreviewMock()
}

@Preview(heightDp = 360, widthDp = 600)
@Composable
fun DialogDiceComposablePreviewMockLandscape() {
    DialogDiceComposablePreviewMock()
}

@Composable
fun DialogDiceComposablePreviewMock() {
    val showDialog = remember { mutableStateOf(true) }

    val bagRepository = BagRepositoryMock
    bagRepository.store(BagSampleData.twoDice)

    val viewModel = DialogDiceViewModel(
        bagRepository,
        0
    )

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            DialogDiceLayout(
                showDialog,
                viewModel,
                DiceSliderSidesMock(),
                DiceSliderPenaltyBonusMock(),
            )
        }
    }
}
