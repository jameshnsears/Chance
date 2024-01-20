package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModelTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import io.mockk.mockk

@Preview(heightDp = 900)
@Composable
fun BagCardSideComposablePreview() {
    val bagRepository = BagRepositoryTestDouble.getInstance()
    bagRepository.store(
        listOf(
            BagDemoSampleData.diceHeadsTails
        )
    )

    val dice = bagRepository.fetch()[0]
    val side = dice.sides[1]

    val dialogBagAndroidViewModelTestDouble = DialogBagAndroidViewModelTestDouble(
        mockk<Application>(),
        bagRepository,
        dice,
        side
    )

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            BagCardSide(
                dialogBagAndroidViewModelTestDouble
            )
        }
    }
}
