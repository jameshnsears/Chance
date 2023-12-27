package com.github.jameshnsears.chance

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import com.github.jameshnsears.chance.data.bag.demo.BagDemo
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.ui.tab.TabRowChance
import com.github.jameshnsears.chance.ui.tab.bag.TabBagViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTree
import timber.log.Timber


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // lock to portrait as Bottom Sheets don't look too good!
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        initLogging()

        val bagRepository = BagRepositoryMock
        bagRepository.store(BagDemo.dice)

        setContent {
            ChanceTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    TabRowChance(
                        TabBagViewModel(bagRepository)
                    )

//                    ZoomAnimationComposable()


//                    val showDialog = mutableStateOf(true)
//                    val bagRepository = BagRepositoryMock
//                    bagRepository.store(BagSampleData.twoDice)
//                    DialogBagLayout(
//                        showDialog,
//                        DialogBagViewModel(
//                            bagRepository,
//                            0
//                        )
//                    )
                }
            }
        }
    }

    private fun initLogging() {
        if (Timber.treeCount == 0) {
            Timber.plant(LoggingLineNumberTree())
        }
    }
}
