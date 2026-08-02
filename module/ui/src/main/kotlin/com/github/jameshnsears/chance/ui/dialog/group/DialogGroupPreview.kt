package com.github.jameshnsears.chance.ui.dialog.group

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.setup.groups.GroupsAndroidViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun DialogGroupPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val showDialog = remember { mutableStateOf(value = true) }

    val repositoryFactory = RepositoryFactory()
    val repositoryBag = repositoryFactory.repositoryBag
    val repositoryGroup = repositoryFactory.repositoryGroup
    val repositoryRoll = repositoryFactory.repositoryRoll

    val context = LocalContext.current
    val application = context.applicationContext as? Application ?: object : Application() {
        override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
            return context.getSharedPreferences(name, mode)
        }
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            DialogGroupContent(
                GroupsAndroidViewModel(
                    application,
                    repositoryBag,
                    repositoryGroup,
                    repositoryRoll,
                ),
                showDialog
            )
        }
    }
}
