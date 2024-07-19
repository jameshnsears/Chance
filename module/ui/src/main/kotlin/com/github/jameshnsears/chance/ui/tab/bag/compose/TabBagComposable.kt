package com.github.jameshnsears.chance.ui.tab.bag.compose

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.BottomSheetScaffoldState
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.github.jameshnsears.chance.ui.tab.bag.ExportImportStatus
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.bag.TabBagState
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.bag.compose.ZoomBag
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TabBagTestTag {
    companion object {
        const val RESIZE = "RESIZE"
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
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 110.dp,
        sheetContent = {
            TabBagBottomSheetLayout(
                bottomSheetScaffoldState,
                tabBagAndroidViewModel,
                zoomAndroidViewModel,
            )
        },
    ) {
        ZoomBag(
            zoomAndroidViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBagBottomSheetLayout(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    val stateFlowTabBag =
        tabBagAndroidViewModel.stateFlowTabBag.collectAsStateWithLifecycle(
            // https://issuetracker.google.com/issues/336842920
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(start = 8.dp, end = 8.dp)
            .height(210.dp),
    ) {
        Resize(
            stateFlowTabBag,
            tabBagAndroidViewModel::resize,
            zoomAndroidViewModel::resizeView
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        Version(bottomSheetScaffoldState)

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        ImportExport(
            bottomSheetScaffoldState,
            tabBagAndroidViewModel,
            zoomAndroidViewModel
        )
    }
}

@Composable
fun Resize(
    state: State<TabBagState>,
    tabBagSlider: (Int) -> Unit,
    zoomResize: (Int) -> Unit
) {
    var resize = state.value.resize

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Slider(
            value = resize.toFloat(),
            onValueChange = {
                resize = it.toInt()
                tabBagSlider(it.toInt())
                zoomResize(it.toInt())
            },
            valueRange = 1f..7f,
            steps = 5,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.testTag(TabBagTestTag.RESIZE)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportExport(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    val stateFlowTabBagExport =
        tabBagAndroidViewModel.stateFlowTabBagExport.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )
    val exportStatus = stateFlowTabBagExport.value.exportStatus
    val exportSuccessfulToast = stringResource(R.string.tab_bag_export_success)

    val stateFlowTabBagImport =
        tabBagAndroidViewModel.stateFlowTabBagImport.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )
    val importStatus = stateFlowTabBagImport.value.importStatus
    val importFailureReason = stateFlowTabBagImport.value.importDetail

    val importSuccessfulToast = stringResource(R.string.tab_bag_import_success)
    val importFailureReasonToast = stringResource(
        R.string.tab_bag_import_failure,
        importFailureReason
    )

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val launcherImport =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                // import is a bit too fast to show a "please wait" Toast; for the moment
                tabBagAndroidViewModel.importFileJson(uri)
            }
        }

    val launcherExport =
        rememberLauncherForActivityResult(contract = CreateDocument("*/json")) { uri: Uri? ->
            uri?.let {
                tabBagAndroidViewModel.exportFileJson(uri)
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
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.partialExpand()
                }

                launcherImport.launch(arrayOf("application/json"))
            },
            modifier = Modifier
                .width(160.dp)
                .testTag(TabBagTestTag.IMPORT),
        ) {
            Icon(
                painterResource(id = R.drawable.publish_fill0_wght400_grad0_opsz24),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_bag_import))
        }

        Spacer(modifier = Modifier.padding(horizontal = 10.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.partialExpand()
                }

                val sdf = SimpleDateFormat("HHmmss", Locale.getDefault())
                val formattedDate = sdf.format(Date())
                launcherExport.launch("Chance-${formattedDate}.json")
            },
            modifier = Modifier
                .width(160.dp)
                .testTag(TabBagTestTag.EXPORT),
        ) {
            Icon(
                painterResource(id = R.drawable.upload_fill0_wght400_grad0_opsz24),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_bag_export))
        }
    }

    if (exportStatus == ExportImportStatus.SUCCESS) {
        Toast.makeText(context, exportSuccessfulToast, Toast.LENGTH_SHORT).show()
    }

    /////////////////

    if (importStatus == ExportImportStatus.SUCCESS) {
        Toast.makeText(context, importSuccessfulToast, Toast.LENGTH_SHORT).show()
        zoomAndroidViewModel.refreshAfterImport()
    }

    if (importStatus == ExportImportStatus.FAILURE) {
        if (importFailureReasonToast != "")
            Toast.makeText(
                context,
                importFailureReasonToast,
                Toast.LENGTH_LONG
            ).show()
    }

    /////////////////

    if (exportStatus != ExportImportStatus.NONE || importStatus != ExportImportStatus.NONE) {
        tabBagAndroidViewModel.resetExportImportStatus()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Version(
    bottomSheetScaffoldState:
    BottomSheetScaffoldState
) {
    val context = LocalContext.current

    val isDarkTheme = isSystemInDarkTheme()

    val coroutineScope = rememberCoroutineScope()

    val githubProjectUrl = "https://github.com/jameshnsears/chance"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 12.dp),
    ) {
        SelectionContainer {
            Text(
                text = BuildConfig.VERSION + "-" + BuildConfig.FLAVOR + "/" + BuildConfig.GIT_HASH,
                fontSize = 14.sp,
            )
        }

        Spacer(Modifier.weight(1f))

        if (isDarkTheme) {
            Image(
                painter = painterResource(id = R.drawable.github_logo_white),
                contentDescription = "",
                modifier = Modifier
                    .height(24.dp)
                    .clickable {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.partialExpand()
                        }

                        openUrlInBrowser(
                            context,
                            githubProjectUrl
                        )
                    },
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.github_logo),
                contentDescription = "",
                modifier = Modifier
                    .height(24.dp)
                    .clickable {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.partialExpand()
                        }

                        openUrlInBrowser(
                            context,
                            githubProjectUrl
                        )
                    },
            )
        }
    }
}

fun openUrlInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(context, intent, null)
}
