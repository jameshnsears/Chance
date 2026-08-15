package com.github.jameshnsears.chance.ui.dialog.dice

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
@Preview(widthDp = 700, heightDp = 1800)
@Composable
fun DialogBagPreview() {
    val context = LocalContext.current
    UtilityFeature.enabled = setOf(
        Flag.REPO_SQLDELIGHT_TEST_DOUBLE,
    )

    val showDialog = mutableStateOf(true)

    val repositoryFactory = RepositoryFactory(context)
    val repositoryBag = repositoryFactory.repositoryBag

    runBlocking(Dispatchers.Main) {
        repositoryBag.store(
            mutableListOf(
                BagDataTestDouble().diceStory,
            )
        )
    }

    val dice = BagDataTestDouble().diceStory
    val sides = dice.sides[0]

    val application = object : Application() {
        override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
            return context.getSharedPreferences(name, mode)
        }
    }

    val dialogDiceAndroidViewModel = runBlocking(Dispatchers.Main) {
        DialogDiceAndroidViewModel(
            application,
            repositoryBag,
            dice,
            sides,
        )
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            DialogBagTabLayout(
                onClose = { showDialog.value = false },
                dialogDiceAndroidViewModel,
            )
        }
    }
}
