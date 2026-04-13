package com.github.jameshnsears.chance.ui.dialog.bag

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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

@SuppressLint("UnrememberedMutableState")
@Preview(widthDp = 700, heightDp = 1800)
@Composable
fun DialogBagPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val showDialog = mutableStateOf(true)

    val repositoryBagProtocolBufferTestDouble =
        RepositoryBagProtocolBufferTestDouble.getInstance(RepositoryFactory().bagDataTestDouble.allDice)
    runBlocking(Dispatchers.Main) {
        repositoryBagProtocolBufferTestDouble.store(
            mutableListOf(
                BagDataTestDouble().diceStory,
            )
        )
    }

    val dice = BagDataTestDouble().diceStory
    val sides = dice.sides[0]

    val dialogBagAndroidViewModel = runBlocking(Dispatchers.Main) {
        DialogBagAndroidViewModel(
            mockk<Application>(),
            repositoryBagProtocolBufferTestDouble,
            dice,
            sides,
        )
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            DialogBagTabLayout(
                showDialog,
                dialogBagAndroidViewModel,
            )
        }
    }
}
