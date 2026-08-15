package com.github.jameshnsears.chance.ui.dialog.dice.card.face

import android.app.Application
import android.content.SharedPreferences
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

@Preview(widthDp = 400, heightDp = 800)
@Composable
fun BagCardSidePreview() {
    val context = LocalContext.current
    UtilityFeature.enabled = setOf(
        Flag.REPO_SQLDELIGHT_TEST_DOUBLE,
    )

    val repositoryFactory = RepositoryFactory(context)
    val repositoryBag = repositoryFactory.repositoryBag
    runBlocking(Dispatchers.Main) {
        repositoryBag.store(
            mutableListOf(
                BagDataTestDouble().diceStory,
            ),
        )
    }

    val dice = BagDataTestDouble().diceStory

    val application = object : Application() {
        override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
            return context.getSharedPreferences(name, mode)
        }
    }

    val cardSideService = runBlocking(Dispatchers.Main) {
        CardSideService(
            application,
            dice.sides[0],
        )
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            BagCardSide(
                cardSideService,
            )
        }
    }
}
