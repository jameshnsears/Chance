package com.github.jameshnsears.chance.ui.dialog.dice.card.dice

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun BagCardDicePreview() {
    val context = LocalContext.current
    UtilityFeature.enabled = setOf(
        Flag.REPO_SQLDELIGHT_TEST_DOUBLE,
    )

    val repositoryFactory = RepositoryFactory(context)
    val repositoryBag = repositoryFactory.repositoryBag
    val repositoryRoll = repositoryFactory.repositoryRoll

    runBlocking(Dispatchers.Main) {
        repositoryBag.store(
            mutableListOf(
                BagDataTestDouble().d6,
            ),
        )
    }

    val cardDiceService = runBlocking(Dispatchers.Main) {
        CardDiceService(
            repositoryBag,
            repositoryRoll,
            BagDataTestDouble().diceStory
        )
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            BagCardDice(
                cardDiceService,
            )
        }
    }
}
