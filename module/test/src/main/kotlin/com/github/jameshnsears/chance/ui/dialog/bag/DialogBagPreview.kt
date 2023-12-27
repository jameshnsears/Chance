package com.github.jameshnsears.chance.ui.dialog.bag

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import com.github.jameshnsears.chance.ui.theme.ChanceTheme

@SuppressLint("UnrememberedMutableState")
@Preview(heightDp = 1000)
@Composable
fun DialogBagPreview() {
    val showDialog = mutableStateOf(true)

    val bagRepository = BagRepositoryMock
    bagRepository.store(BagSampleData.twoDice)

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            DialogBagLayout(
                showDialog,
                DialogBagViewModel(
                    bagRepository,
                    0
                ),
            )
        }
    }
}
