package com.github.jameshnsears.chance.ui.tab.bag.composable

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.BuildConfig
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.dialog.confirm.composable.DialogConfirm
import com.github.jameshnsears.chance.ui.tab.bag.ExportImportStatus
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.bag.composable.ZoomBag
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
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel
) {
    TabBagLayout(tabBagAndroidViewModel, zoomBagAndroidViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBagLayout(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 110.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                TabBagBottomSheetLayout(
                    bottomSheetScaffoldState,
                    tabBagAndroidViewModel,
                    zoomBagAndroidViewModel,
                )
            }
        }
    ) {
        ZoomBag(
            zoomBagAndroidViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBagBottomSheetLayout(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel
) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(start = 8.dp, end = 8.dp)
            .height(300.dp),
    ) {
        Resize(
            tabBagAndroidViewModel,
            zoomBagAndroidViewModel,
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
            zoomBagAndroidViewModel
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        ResetStorage(
            tabBagAndroidViewModel
        )
    }
}

@Composable
fun Resize(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel,
) {
    val stateFlowResize =
        tabBagAndroidViewModel.stateFlowResize.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Slider(
            value = stateFlowResize.value.toFloat(),
            onValueChange = { newValue ->
                tabBagAndroidViewModel.resizeSettings(newValue.toInt())
                zoomBagAndroidViewModel.resizeView(newValue.toInt())
            },
            valueRange = 1f..9f,
            steps = 7,
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
fun ResetStorage(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
) {
    val showDialogConfirm = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                showDialogConfirm.value = true
            },
            modifier = Modifier
                .width(200.dp)
                .testTag(TabBagTestTag.IMPORT),
        ) {
            Icon(
                painterResource(id = R.drawable.baseline_restart_alt_24),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_bag_reset_storage))
        }
    }

    if (showDialogConfirm.value) {
        DialogConfirm(
            openDialog = showDialogConfirm.value,
            onDismissRequest = {
                showDialogConfirm.value = false
            },
            onConfirmation = {
                tabBagAndroidViewModel.resetStorage()
                showDialogConfirm.value = false
            },
            title = stringResource(R.string.tab_bag_reset_storage),
            text = stringResource(R.string.tab_bag_reset_storage_confirm)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportExport(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel
) {
    val stateFlowTabBagExport =
        tabBagAndroidViewModel.stateFlowTabBagExport.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )
    val exportStatus = stateFlowTabBagExport.value.exportStatus
    val exportSuccessfulToast = stringResource(R.string.tab_bag_export_success)

    val stateFlowTabBagImport =
        tabBagAndroidViewModel.stateFlowTabBagImport.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
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

                launcherImport.launch(arrayOf("application/json", "*/json"))
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
        zoomBagAndroidViewModel.refreshAfterImport()
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
                // Baseline variant (fdroidRelease + fdroidBenchmarkRelease) on API 35 emulator
                // text = "",
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
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}
