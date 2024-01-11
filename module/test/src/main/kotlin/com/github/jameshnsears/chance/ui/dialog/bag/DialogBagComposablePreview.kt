package com.github.jameshnsears.chance.ui.dialog.bag

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.repository.bag.BagDemoData
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import io.mockk.mockk

@SuppressLint("UnrememberedMutableState")
@Preview(heightDp = 850)
@Composable
fun DialogBagComposablePreview() {
    val showDialog = mutableStateOf(true)

    val bagRepository = BagRepositoryTestDouble.getInstance()
    bagRepository.store(
        listOf(
            BagDemoData.diceHeadsTails
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
            DialogBagLayout(
                showDialog,
                dialogBagAndroidViewModelTestDouble
            )
        }
    }
}

