package com.github.jameshnsears.chance

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.roll.repository.RollRepositoryMock
import com.github.jameshnsears.chance.data.roll.sample.RollSampleData
import com.github.jameshnsears.chance.ui.tab.TabRowChance
import com.github.jameshnsears.chance.ui.tab.bag.TabBagViewModel
import com.github.jameshnsears.chance.ui.tab.roll.TabRollViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTree
import timber.log.Timber


class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        initLogging()

        val bagRepository = BagRepositoryMock
        bagRepository.store(
            listOf(
                BagDemoData.diceHeadsTails
            )
        )

        val rollRepository = RollRepositoryMock
        rollRepository.store(RollSampleData.rollHistory_roll1Sequence1)

        setContent {
            ChanceTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    TabRowChance(
                        TabBagViewModel(bagRepository),
                        TabRollViewModel(RollRepositoryMock)
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
