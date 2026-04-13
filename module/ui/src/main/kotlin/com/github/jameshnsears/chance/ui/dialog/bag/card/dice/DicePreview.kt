package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.UtilityPreview
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.repo.impl.bag.testdouble.RepositoryBagProtocolBufferTestDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@SuppressLint("UnrememberedMutableState")
@UtilityPreview
@Composable
fun BagCardDicePreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositoryBagProtocolBufferTestDouble =
        RepositoryBagProtocolBufferTestDouble.getInstance(RepositoryFactory(LocalContext.current).bagDataTestDouble.allDice)
    runBlocking(Dispatchers.Main) {
        repositoryBagProtocolBufferTestDouble.store(
            mutableListOf(
                BagDataTestDouble().d6,
            ),
        )
    }

    val cardDiceService = runBlocking(Dispatchers.Main) {
        CardDiceService(
            repositoryBagProtocolBufferTestDouble,
            BagDataTestDouble().diceStory
        )
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            BagCardDice(
                cardDiceService,
            )
        }
    }
}
