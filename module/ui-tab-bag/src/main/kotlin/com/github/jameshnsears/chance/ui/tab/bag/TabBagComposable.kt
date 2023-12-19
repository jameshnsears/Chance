package com.github.jameshnsears.chance.ui.tab.bag

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.data.bag.demo.BagDemo
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagViewModel
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomColumn

@Composable
fun TabBag() {
    TabBagLayout()
}

@Composable
fun TabBagLayout() {
    Column(modifier = Modifier.padding(10.dp)) {
        ElevatedCard(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                DemoBag()

                ImportExport()
            }
        }

        val bagRepository = BagRepositoryMock
        bagRepository.store(BagDemo.dice)

        ZoomColumn(ZoomBagViewModel(bagRepository))
    }

    BottomSheet()
}

@Composable
fun DemoBag() {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_bag_demo_bag)
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}

@Composable
fun ImportExport() {
    val importIcon = painterResource(id = R.drawable.publish_fill0_wght400_grad0_opsz24)
    val exportIcon = painterResource(id = R.drawable.upload_fill0_wght400_grad0_opsz24)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier.testTag("bagButtonExport")
        ) {
            Icon(
                exportIcon,
                contentDescription = stringResource(R.string.tab_bag_export),
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_bag_export))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier.testTag("bagButtonImport")
        ) {
            Icon(
                importIcon,
                contentDescription = stringResource(R.string.tab_bag_import),
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_bag_import))
        }
    }
}

@Composable
fun Slider() {
    var sliderPosition by remember { mutableStateOf(0f) }
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it },
        valueRange = 0f..100f,
        onValueChangeFinished = {
            // launch some business logic update with the state you hold
            // viewModel.updateSelectedSliderValue(sliderPosition)
        },
        steps = 5,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 32.dp,
        sheetContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(64.dp),
            ) {
                Slider()
            }
        }) { innerPadding ->
//        Box(Modifier.padding(innerPadding)) {
//            Text("Scaffold Content")
//        }
    }
}
