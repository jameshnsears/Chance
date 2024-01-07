package com.github.jameshnsears.chance.ui.dialog.bag

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@SuppressLint("UnrememberedMutableState")
@Preview(heightDp = 900)
@Composable
fun DialogBagComposablePreview() {
    val showDialog = mutableStateOf(true)

    val bagRepository = BagRepositoryMock
    bagRepository.store(
        listOf(
            BagDemoData.diceHeadsTails
        )
    )

    val dice = bagRepository.fetch()[0]
    val side = dice.sides[1]

    val viewModel = DialogBagAndroidViewModelMock(
        bagRepository,
        dice,
        side
    )

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            DialogBagLayout(
                showDialog,
                viewModel
            )
        }
    }
}

