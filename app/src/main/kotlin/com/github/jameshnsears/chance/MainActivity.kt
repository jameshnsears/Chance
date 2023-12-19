package com.github.jameshnsears.chance

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import com.github.jameshnsears.chance.data.bag.demo.BagDemo
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagLayout
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTree
import timber.log.Timber


class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Timber.treeCount == 0) {
            Timber.plant(LoggingLineNumberTree())
        }

        val bagRepository = BagRepositoryMock
        bagRepository.store(BagDemo.dice)

        setContent {
            ChanceTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
//                    ZoomAnimationComposable()

//                    TabRowChance()

//                    TabRollLayout()

//                    TabBagLayout()

                    val showDialog = mutableStateOf(true)
                    val bagRepository = BagRepositoryMock
                    bagRepository.store(BagSampleData.twoDice)
                    DialogBagLayout(
                        showDialog,
                        DialogBagViewModel(
                            bagRepository,
                            0
                        )
                    )
                }
            }
        }
    }
}
