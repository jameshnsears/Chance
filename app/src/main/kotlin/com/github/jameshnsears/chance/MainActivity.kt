package com.github.jameshnsears.chance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.jameshnsears.chance.data.repository.dice.DiceRepositoryMock
import com.github.jameshnsears.chance.data.repository.dice.sample.DiceSampleData
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDice
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTree
import com.squareup.leakcanary.core.BuildConfig
import timber.log.Timber


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG && Timber.treeCount == 0) {
            Timber.plant(LoggingLineNumberTree())
        }

        val diceRepository = DiceRepositoryMock
        diceRepository.store(DiceSampleData.twoDice)

        /*
Remember that when using a singleton repository, it's crucial to ensure that the repository doesn't hold onto any resources that need to be released when the application shuts down. This could include database connections, file handles, or network connections.
         */

        setContent {
            ChanceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val showDialog = remember { mutableStateOf(false) }

                    val viewModel = DialogDiceViewModel(
                        diceRepository,
                        0
                    )

                    Button(
                        onClick = {
                            //Timber.d("james")
                            showDialog.value = true
                        }
                    ) {
                        Text("Show Dialog")
                    }

                    if (showDialog.value) {
                        DialogDice(
                            showDialog,
                            viewModel
                        )
                    }
                }
            }
        }
    }
}
