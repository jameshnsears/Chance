package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.repo.impl.bag.testdouble.RepositoryBagProtocolBufferTestDouble
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@Preview(widthDp = 400, heightDp = 800)
@Composable
fun BagCardSidePreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositoryBagProtocolBufferTestDouble =
        RepositoryBagProtocolBufferTestDouble.getInstance(RepositoryFactory(LocalContext.current).bagDataTestDouble.allDice)
    runBlocking(Dispatchers.Main) {
        repositoryBagProtocolBufferTestDouble.store(
            mutableListOf(
                BagDataTestDouble().diceStory,
            ),
        )
    }

    val dice = BagDataTestDouble().diceStory

    val cardSideAndroidViewModel = runBlocking(Dispatchers.Main) {
        CardSideAndroidViewModel(
            mockk<Application>(),
            dice.sides[0],
        )
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            BagCardSide(
                cardSideAndroidViewModel,
            )
        }
    }
}
