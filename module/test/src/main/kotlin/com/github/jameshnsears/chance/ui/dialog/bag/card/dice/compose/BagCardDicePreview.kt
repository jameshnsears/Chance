package com.github.jameshnsears.chance.ui.dialog.bag.card.dice.compose

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagStartup
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.CardDiceAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@SuppressLint("UnrememberedMutableState")
@UtilityPreview
@Composable
fun BagCardDicePreview() {
    val repositoryBagTestDouble = RepositoryBagTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryBagTestDouble.store(
            mutableListOf(
                SampleBagStartup.diceHeadsTails,
            ),
        )
    }

    val cardDiceAndroidViewModel = runBlocking {
        CardDiceAndroidViewModel(
            mockk<Application>(),
            repositoryBagTestDouble,
            SampleBagStartup.diceStory
        )
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            BagCardDice(
                cardDiceAndroidViewModel,
            )
        }
    }
}
