package com.github.jameshnsears.chance.ui.dialog.dice.card.dice

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.repo.impl.bag.testdouble.RepositoryBagProtocolBufferTestDouble
import com.github.jameshnsears.chance.data.repo.impl.roll.testdouble.RepositoryRollProtocolBufferTestDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun BagCardDicePreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositoryBagProtocolBufferTestDouble =
        RepositoryBagProtocolBufferTestDouble.getInstance()

    val bagDataTestDouble = BagDataTestDouble()
    val repositoryRollProtocolBufferTestDouble =
        RepositoryRollProtocolBufferTestDouble.getInstance(
            RollHistoryDataTestDouble(
                bagDataTestDouble,
                GroupDataTestDouble(bagDataTestDouble)
            ).rollHistory,
            repositoryBagProtocolBufferTestDouble
        )

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
            repositoryRollProtocolBufferTestDouble,
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
