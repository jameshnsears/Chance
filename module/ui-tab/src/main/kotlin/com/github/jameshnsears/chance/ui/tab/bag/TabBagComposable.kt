package com.github.jameshnsears.chance.ui.tab.bag

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.github.jameshnsears.chance.ui.tab.BuildConfig
import com.github.jameshnsears.chance.ui.tab.R
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBag
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagViewModel

class TabBagTestTag {
    companion object {
        const val export = "export"
        const val import = "import"
    }
}

@Composable
fun TabBag(tabBagViewModel: TabBagViewModel) {
    TabBagLayout(tabBagViewModel)
}

@Composable
fun TabBagLayout(tabBagViewModel: TabBagViewModel) {
    Column(modifier = Modifier.padding(10.dp)) {
        TopCard()

        ZoomBag(
            ZoomBagViewModel(tabBagViewModel.bagRepository)
        )
    }

    TabBagBottomSheet()
}

@Composable
fun TopCard() {
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
}

@Composable
fun DemoBag() {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable {
                checked = !checked
            }
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
            .padding(start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier
                .width(150.dp)
                .testTag(TabBagTestTag.export)
        ) {
            Icon(
                exportIcon,
                contentDescription = stringResource(R.string.tab_bag_export),
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_bag_export))
        }

        Spacer(modifier = Modifier.padding(horizontal = 10.dp))

        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier
                .width(150.dp)
                .testTag(TabBagTestTag.import)
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
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(stringResource(R.string.tab_bag_zoom))

        Spacer(modifier = Modifier.width(10.dp))

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..100f,
            onValueChangeFinished = {
            },
            steps = 5,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBagBottomSheet() {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 32.dp,
        sheetContent = {
            TabBagBottomSheetLayout()
        }) {
    }
}

@Composable
fun VersionDetails() {
    val v = BuildConfig.GIT_HASH

    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Text(
            text = v + "-" + BuildConfig.FLAVOR,
            fontSize = 14.sp
        )

        Spacer(Modifier.weight(1f))

        val drawable = painterResource(id = R.drawable.github_logo)
        Image(
            painter = drawable,
            contentDescription = "",
            modifier = Modifier.clickable {
                openUrlInBrowser(
                    context,
                    "https://github.com/jameshnsears/chance"
                )
            }
        )
    }
}

fun openUrlInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(context, intent, null)
}

@Composable
fun TabBagBottomSheetLayout() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(80.dp),
    ) {
        Slider()

        VersionDetails()
    }
}