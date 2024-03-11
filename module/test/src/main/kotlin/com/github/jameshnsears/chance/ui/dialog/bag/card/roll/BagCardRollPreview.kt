package com.github.jameshnsears.chance.ui.dialog.bag.card.roll

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagStartup
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@UtilityPreview
@Composable
fun BagCardRollPreview() {
    val repositoryBagTestDouble = RepositoryBagTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryBagTestDouble.store(
            mutableListOf(
                SampleBagStartup.diceHeadsTails,
            ),
        )
    }

    val dice = SampleBagStartup.diceStory

    val bagCardRollViewModel = runBlocking {
        BagCardRollViewModel(
            dice
        )
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            BagCardRoll(
                bagCardRollViewModel,
            )
        }
    }
}
