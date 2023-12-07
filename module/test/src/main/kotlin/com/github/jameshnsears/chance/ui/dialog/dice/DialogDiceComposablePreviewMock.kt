package com.github.jameshnsears.chance.ui.dialog.dice

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.repository.dice.DiceRepositoryMock
import com.github.jameshnsears.chance.data.repository.dice.sample.DiceSampleData
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

    val diceRepository = DiceRepositoryMock
    diceRepository.store(DiceSampleData.twoDice)

    val viewModel = DialogDiceViewModel(
        diceRepository,
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
