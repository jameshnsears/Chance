package com.github.jameshnsears.chance.ui.tab.bag.compose

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.BuildConfig
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.bag.TabBagState
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.bag.compose.ZoomBag
import kotlin.math.round

class TabBagTestTag {
    companion object {
        const val SCALE = "SCALE"
        const val EXPORT = "EXPORT"
        const val IMPORT = "IMPORT"
    }
}

@Composable
fun TabBag(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    TabBagLayout(tabBagAndroidViewModel, zoomAndroidViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBagLayout(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 110.dp,
        sheetContent = {
            TabBagBottomSheetLayout(tabBagAndroidViewModel, zoomAndroidViewModel)
        },
    ) {
        ZoomBag(zoomAndroidViewModel)
    }
}

@Composable
fun TabBagBottomSheetLayout(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    val stateFlow =
        tabBagAndroidViewModel.stateFlow.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(150.dp),
    ) {
        Resize(
            stateFlow,
            tabBagAndroidViewModel::resizeSlider,
            zoomAndroidViewModel::resizeView
        )

        HorizontalDivider()

        ImportExport(
            tabBagAndroidViewModel::importFileJson,
            tabBagAndroidViewModel::exportFileJson,
        )

        HorizontalDivider()

        Version()
    }
}

@Composable
fun Resize(
    state: State<TabBagState>,
    tabBagSlider: (Float) -> Unit,
    zoomResize: (Int) -> Unit
) {
    var resize = state.value.resize

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.tab_bag_scale))

        Spacer(modifier = Modifier.width(10.dp))

        Slider(
            value = resize,
            onValueChange = {
                resize = it
                tabBagSlider(it)
                zoomResize(round(it).toInt())
            },
            valueRange = 1f..7f,
            steps = 5,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.testTag(TabBagTestTag.SCALE)
        )
    }
}

@Composable
fun ImportExport(
    importFileJson: (Uri) -> Unit,
    exportFileJson: (Uri) -> Unit
) {
    val launcherImportResult = remember { mutableStateOf<Uri?>(null) }
    val launcherImport =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            launcherImportResult.value = uri
        }
    launcherImportResult.value?.let { uri ->
        importFileJson(uri)
    }

    val launcherExport =
        rememberLauncherForActivityResult(contract = CreateDocument("*/json")) { uri: Uri? ->
            uri?.let {
                exportFileJson(uri)
            }
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                launcherExport.launch("filename.json")
            },
            modifier = Modifier
                .width(150.dp)
                .testTag(TabBagTestTag.EXPORT),
        ) {
            Icon(
                painterResource(id = R.drawable.upload_fill0_wght400_grad0_opsz24),
                contentDescription = stringResource(R.string.tab_bag_export),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_bag_export))
        }

        Spacer(modifier = Modifier.padding(horizontal = 10.dp))

        Button(
            onClick = {
                launcherImport.launch(arrayOf("*/json"))
            },
            modifier = Modifier
                .width(150.dp)
                .testTag(TabBagTestTag.IMPORT),
        ) {
            Icon(
                painterResource(id = R.drawable.publish_fill0_wght400_grad0_opsz24),
                contentDescription = stringResource(R.string.tab_bag_import),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_bag_import))
        }
    }
}

@Composable
fun Version() {
    val gitHash = BuildConfig.GIT_HASH

    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) {
        SelectionContainer {
            Text(
                text = gitHash + "-" + BuildConfig.FLAVOR,
                fontSize = 14.sp,
            )
        }

        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.github_logo),
            contentDescription = "",
            modifier = Modifier.clickable {
                openUrlInBrowser(
                    context,
                    "https://github.com/jameshnsears/chance",
                )
            },
        )
    }
}

fun openUrlInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(context, intent, null)
}
