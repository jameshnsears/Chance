package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.github.jameshnsears.chance.ui.R


@Composable
fun SideImageSVG(
    cardSideAndroidViewModel: CardSideAndroidViewModel
) {
    val context = LocalContext.current

    val sideImageError = stringResource(R.string.dialog_bag_side_image_error)

    val launcherImport = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        try {
            if (uri != null) {
                cardSideAndroidViewModel.sideImageSvgImport(uri)
            }
        } catch (_: CardSideSvgImportException) {
            Toast.makeText(
                context,
                sideImageError,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val stateFlowCardSide =
        cardSideAndroidViewModel.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val sideImageDrawableId = stateFlowCardSide.value.sideImageDrawableId

    val sideImageBase64Request = stateFlowCardSide.value.sideImageRequest

    val diceSidesFewerThanSideNumber = stateFlowCardSide.value.diceSidesFewerThanSideNumber

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Button(
                onClick = {
                    launcherImport.launch(arrayOf("image/svg+xml"))
                },
                modifier = Modifier
                    .width(180.dp)
                    .padding(bottom = 6.dp)
                    .testTag(SideTestTag.SIDE_IMAGE_SVG),
                enabled = !diceSidesFewerThanSideNumber
            ) {
                Icon(
                    painterResource(id = R.drawable.side_image),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )

                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Text(stringResource(R.string.dialog_bag_side_image))
            }

            Button(
                onClick = {
                    cardSideAndroidViewModel.sideImageSvgClear()
                },
                modifier = Modifier
                    .width(180.dp)
                    .padding(top = 6.dp)
                    .testTag(SideTestTag.SIDE_IMAGE_SVG),
                enabled = cardSideAndroidViewModel.sideImageAvailable() && !diceSidesFewerThanSideNumber
            ) {
                Icon(
                    painterResource(id = R.drawable.side_image_reset),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )

                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Text(stringResource(R.string.dialog_bag_side_image_clear))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (sideImageDrawableId != 0) {
            Image(
                painter = painterResource(id = sideImageDrawableId),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
            )
        } else {
            if (sideImageBase64Request != null) {
                AsyncImage(
                    model = sideImageBase64Request,
                    contentDescription = "",
                    modifier = Modifier
                        .size(100.dp)
                )
            }
        }
    }

    SideDescriptionAndColourApplyToAll(
        stateFlowCardSide.value.sideApplyToAllSvg,
        SideTestTag.SIDE_APPLY_SVG,
        cardSideAndroidViewModel::sideApplyToAllSvg,
        diceSidesFewerThanSideNumber = diceSidesFewerThanSideNumber
    )

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "",
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
        )
    }
}
