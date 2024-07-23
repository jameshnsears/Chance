package com.github.jameshnsears.chance.ui.dialog.bag.card.dice.compose

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.repository.bag.testdouble.RepositoryBagTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.CardDiceViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@SuppressLint("UnrememberedMutableState")
@UtilityPreview
@Composable
fun BagCardDicePreview() {
    UtilityFeature.enabled = setOf(
        UtilityFeature.Flag.NONE,
    )

    val repositoryBagTestDouble =
        RepositoryBagTestDouble.getInstance(RepositoryFactory().bagDataTestDouble.allDice)
    runBlocking(Dispatchers.Main) {
        repositoryBagTestDouble.store(
            mutableListOf(
                BagDataTestDouble().d6,
            ),
        )
    }

    val cardDiceViewModel = runBlocking {
        CardDiceViewModel(
            repositoryBagTestDouble,
            BagDataTestDouble().diceStory
        )
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            BagCardDice(
                cardDiceViewModel,
            )
        }
    }
}
