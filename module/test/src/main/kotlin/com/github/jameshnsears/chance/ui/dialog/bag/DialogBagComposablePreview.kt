package com.github.jameshnsears.chance.ui.dialog.bag

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryTestDouble
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@SuppressLint("UnrememberedMutableState")
@Preview(heightDp = 900)
@Composable
fun DialogBagComposablePreview() {
    val showDialog = mutableStateOf(true)

    val bagRepositoryTestDouble = DiceBagRepositoryTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        bagRepositoryTestDouble.store(
            listOf(
                BagDemoSampleData.diceHeadsTails,
            ),
        )
    }

    val dice = BagDemoSampleData.diceStory
    val sides = dice.sides[0]

    val dialogBagAndroidViewModel = runBlocking {
        DialogBagAndroidViewModel(
            mockk<Application>(),
            bagRepositoryTestDouble,
            dice,
            sides,
        )
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            DialogBagLayout(
                showDialog,
                dialogBagAndroidViewModel,
            )
        }
    }
}
