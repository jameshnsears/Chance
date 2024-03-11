package com.github.jameshnsears.chance.ui.dialog.bag

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@SuppressLint("UnrememberedMutableState")
@UtilityPreview
@Composable
fun DialogBagPreview() {
    val showDialog = mutableStateOf(true)

    val repositoryBagTestDouble = RepositoryBagTestDouble.getInstance()
    runBlocking(Dispatchers.Main) {
        repositoryBagTestDouble.store(
            mutableListOf(
                SampleBag.d2,
            )
        )
    }

    val dice = SampleBag.d2
    val sides = dice.sides[0]

    val dialogBagAndroidViewModel = runBlocking {
        DialogBagAndroidViewModel(
            mockk<Application>(),
            repositoryBagTestDouble,
            dice,
            sides,
        )
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            DialogBagLayout(
                showDialog,
                dialogBagAndroidViewModel,
            )
        }
    }
}
