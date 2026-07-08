package com.github.jameshnsears.chance.ui.tab.setup.dice

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.BuildConfig
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Version(
    bottomSheetScaffoldState:
    BottomSheetScaffoldState
) {
    val context = LocalContext.current

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
                // using LocalInspectionMode to avoid NoClassDefFoundError: DiceVersionKt in Preview
                // which is caused by accessing BuildConfig
                text = if (LocalInspectionMode.current) "v1.2.3-debug/1234567" else BuildConfig.VERSION + "-" + BuildConfig.FLAVOR + "/" + BuildConfig.GIT_HASH,
                fontSize = 14.sp,
            )
        }

        Spacer(Modifier.weight(1f))

        val githubPainter = painterResource(
            id = if (isSystemInDarkTheme()) R.drawable.github_logo_white
            else R.drawable.github_logo
        )

        val logoModifier = remember { Modifier.height(24.dp) }

        Image(
            painter = githubPainter,
            contentDescription = stringResource(R.string.github),
            modifier = logoModifier
                .minimumInteractiveComponentSize()
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

fun openUrlInBrowser(context: Context, url: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}
