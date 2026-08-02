package com.github.jameshnsears.chance.ui.group

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import kotlinx.coroutines.runBlocking

@Preview
@Composable
fun GroupPreview() {
    val bagDataTestDouble = BagDataTestDouble()
    val groupDataTestDouble = GroupDataTestDouble(bagDataTestDouble)
    val allDice = runBlocking { bagDataTestDouble.allDice() }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Group(
                    group = groupDataTestDouble.groupHistory[0],
                    diceBag = allDice,
                    initiallyExpanded = false
                )

                Group(
                    group = groupDataTestDouble.groupHistory[1],
                    diceBag = allDice,
                    initiallyExpanded = true
                )
            }
        }
    }
}
