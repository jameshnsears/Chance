package com.github.jameshnsears.chance.ui.dialog.dice

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.repository.dice.DiceRepositoryMock
import com.github.jameshnsears.chance.data.repository.dice.sample.DiceSampleData
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@Preview(showBackground = true)
@Composable
fun DialogDiceLayoutMockPreview() {
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
                DialogDiceSliderSidesMock(),
                DialogDiceSliderPenaltyBonusMock(),
            )
        }
    }
}
