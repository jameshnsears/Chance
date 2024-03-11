package com.github.jameshnsears.chance.ui.tab.bag

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.data.repository.RepositoryImportValidation
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

data class TabBagState(
    var resize: Float
)

class TabBagAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application) {

    private val _stateFlow = MutableStateFlow(
        TabBagState(
            resize = resizeSliderInit()
        )
    )
    val stateFlow: StateFlow<TabBagState> = _stateFlow

    suspend fun export(): String {
        return "[" +
                repositorySettings.exportJson() +
                "," +
                repositoryBag.exportJson() +
                "," +
                repositoryRoll.exportJson() +
                "]"
    }

    private var _importFailed = MutableStateFlow(false)
    var importFailed: StateFlow<Boolean> = _importFailed

    suspend fun import(json: String) {
        viewModelScope.launch {
            try {
                val rootNode = jacksonObjectMapper().readTree(json)

                RepositoryImportValidation.validate(rootNode)

                repositorySettings.importJson(rootNode.get(0).toString())
                repositoryBag.importJson(rootNode.get(1).toString())
                repositoryRoll.importJson(rootNode.get(2).toString())

            } catch (e: Exception) {
                Timber.d(e.message)
                _importFailed.value = true
            }
        }
    }

    fun importFileJson(uri: Uri) {
        Timber.d("${uri.path}")

//        context.contentResolver.openInputStream(uri)?.use { inputStream ->
//            val text = inputStream.reader().readText()
//        }

        // TODO have an i saying must end if .json
    }

    fun exportFileJson(uri: Uri) {
        Timber.d("${uri.path}")
//        val outputStream = getApplication<Application>().contentResolver.openOutputStream(uri)

//            val context = LocalContext.current
//            context.contentResolver.openOutputStream(it)?.use { outputStream ->
//                outputStream.write("Hello, World!".toByteArray())
//            }
    }

    ////////////////////////////////

    fun resizeSlider(scale: Float) {
        _stateFlow.update { it.copy(resize = scale) }
    }

    private fun resizeSliderInit(): Float {
        return 4f
    }
}
