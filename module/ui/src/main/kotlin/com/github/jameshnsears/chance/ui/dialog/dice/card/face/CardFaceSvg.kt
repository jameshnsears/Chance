package com.github.jameshnsears.chance.ui.dialog.dice.card.face

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.github.jameshnsears.chance.common.R
import kotlinx.coroutines.launch


@Composable
fun SideImageSVG(
    cardSideService: CardSideService
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val sideImageError = stringResource(R.string.dialog_bag_side_image_error)

    val launcherImport = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                try {
                    cardSideService.sideImageSvgImport(uri)
                } catch (_: CardSideSvgImportException) {
                    Toast.makeText(
                        context,
                        sideImageError,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    val stateFlowCardSide =
        cardSideService.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val state = stateFlowCardSide.value

    Row(
        modifier = Modifier
            .padding(bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SideImageButtons(cardSideService, state, launcherImport)

        Spacer(modifier = Modifier.weight(1f))

        SideImagePreview(state)
    }

    SideDescriptionAndColourApplyToAll(
        state.sideApplyToAllSvg,
        CardFaceTestTag.SIDE_IMAGE_APPLY_ALL,
        cardSideService::sideApplyToAllSvg,
        diceSidesFewerThanSideNumber = state.diceSidesFewerThanSideNumber
    )

    SideImageInfo()
}

@Composable
private fun SideImageButtons(
    cardSideService: CardSideService,
    state: CardSideState,
    launcherImport: androidx.activity.result.ActivityResultLauncher<Array<String>>
) {
    Column(
        modifier = Modifier.height(128.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                launcherImport.launch(arrayOf("image/svg+xml"))
            },
            modifier = Modifier
                .width(180.dp)
                .padding(bottom = 6.dp)
                .minimumInteractiveComponentSize()
                .testTag(CardFaceTestTag.SIDE_IMAGE_SVG),
            enabled = !state.diceSidesFewerThanSideNumber
        ) {
            Icon(
                painterResource(id = R.drawable.side_image),
                contentDescription = stringResource(R.string.dialog_bag_side_image),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_image))
        }

        Button(
            onClick = {
                cardSideService.sideImageSvgClear()
            },
            modifier = Modifier
                .width(180.dp)
                .padding(top = 6.dp)
                .minimumInteractiveComponentSize()
                .testTag(CardFaceTestTag.SIDE_IMAGE_SVG_DROP),
            enabled = cardSideService.sideImageAvailable() && !state.diceSidesFewerThanSideNumber
        ) {
            Icon(
                painterResource(id = R.drawable.side_image_reset),
                contentDescription = stringResource(R.string.dialog_bag_side_image_clear),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_image_clear))
        }
    }
}

@Composable
private fun SideImagePreview(state: CardSideState) {
    if (state.sideImageDrawableId != 0) {
        Image(
            painter = painterResource(id = state.sideImageDrawableId),
            contentDescription = stringResource(R.string.svg),
            modifier = Modifier
                .size(110.dp)
        )
    } else {
        if (state.sideImageRequest != null) {
            AsyncImage(
                model = state.sideImageRequest,
                contentDescription = stringResource(R.string.svg),
                modifier = Modifier
                    .size(110.dp)
            )
        }
    }
}

@Composable
private fun SideImageInfo() {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(R.string.dialog_bag_side_image_apply_svg_size),
        )
    }

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_side_image_apply_svg_size),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
