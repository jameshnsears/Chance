package com.github.jameshnsears.chance.ui.tab.bag

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.data.domain.state.Settings
import com.github.jameshnsears.chance.data.repository.RepositoryImportException
import com.github.jameshnsears.chance.data.repository.RepositoryImportStatus
import com.github.jameshnsears.chance.data.repository.RepositoryImportValidation
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.tab.SettingsTab
import com.github.jameshnsears.chance.ui.tab.TabSettingsInterface
import com.github.jameshnsears.chance.ui.tab.TabSettingsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

data class TabBagState(
    var resize: Float,
    var tabRowChance: Int
)

enum class ExportImportStatus {
    NONE,
    IMPORT_STARTED,
    SUCCESS,
    FAILURE
}

data class TabBagExportState(
    var exportStatus: ExportImportStatus,
)

data class TabBagImportState(
    var importStatus: ExportImportStatus,
    var importDetail: RepositoryImportStatus
)

class TabBagAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application), TabSettingsInterface {
    private val _stateFlowTabBag = MutableStateFlow(
        TabBagState(
            resize = Settings().resize,
            tabRowChance = Settings().tabRowChance
        )
    )
    val stateFlowTabBag: StateFlow<TabBagState> = _stateFlowTabBag

    init {
        viewModelScope.launch {
            _stateFlowTabBag.update {
                it.copy(
                    resize = TabSettingsModel.resize(repositorySettings),
                    tabRowChance = TabSettingsModel.markTabAsCurrentInSettings(repositorySettings)
                )
            }
        }
    }

    fun resetExportImportStatus() {
        viewModelScope.launch {
            _stateFlowTabBagExport.update {
                it.copy(
                    exportStatus = ExportImportStatus.NONE,
                )
            }

            _stateFlowTabBagImport.update {
                it.copy(
                    importStatus = ExportImportStatus.NONE,
                    importDetail = RepositoryImportStatus.NONE
                )
            }
        }
    }

    suspend fun exportRepositoriesAsJson(): String {
        return "[" +
                repositorySettings.exportJson() +
                "," +
                repositoryBag.exportJson() +
                "," +
                repositoryRoll.exportJson() +
                "]"
    }

    private val _stateFlowTabBagExport = MutableStateFlow(
        TabBagExportState(
            exportStatus = ExportImportStatus.NONE,
        )
    )
    val stateFlowTabBagExport: StateFlow<TabBagExportState> = _stateFlowTabBagExport

    private val _stateFlowTabBagImport = MutableStateFlow(
        TabBagImportState(
            importStatus = ExportImportStatus.NONE,
            importDetail = RepositoryImportStatus.NONE
        )
    )
    val stateFlowTabBagImport: StateFlow<TabBagImportState> = _stateFlowTabBagImport

    fun import(json: String) {
        Timber.d("import.started")

        viewModelScope.launch {
            try {
                val rootNode = jacksonObjectMapper().readTree(json)

                RepositoryImportValidation.validate(rootNode)

                Timber.d("import.validation completed")

                runBlocking {
                    repositorySettings.clear()
                    repositorySettings.importJson(rootNode.get(0).toString())

                    repositoryBag.clear()
                    repositoryBag.importJson(rootNode.get(1).toString())

                    repositoryRoll.clear()
                    repositoryRoll.importJson(rootNode.get(2).toString())
                }

                _stateFlowTabBagImport.update {
                    it.copy(
                        importStatus = ExportImportStatus.SUCCESS,
                        importDetail = RepositoryImportStatus.NONE
                    )
                }

                TabBagImportEvent.emit()

                Timber.d("import.completed.success")
            } catch (e: RepositoryImportException) {
                Timber.e(e.detail.toString())

                _stateFlowTabBagImport.update {
                    it.copy(
                        importStatus = ExportImportStatus.FAILURE,
                        importDetail = e.detail
                    )
                }

                Timber.e("import.completed.failure")
            }
        }
    }

    fun importFileJson(uri: Uri) {
        _stateFlowTabBagImport.update {
            it.copy(
                importStatus = ExportImportStatus.IMPORT_STARTED
            )
        }

        viewModelScope.launch {
            getContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                import(inputStream.reader().readText())
            }
        }
    }

    private fun getContext() = getApplication<Application>().applicationContext

    fun exportFileJson(uri: Uri) {
        viewModelScope.launch {
            getContext().contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(exportRepositoriesAsJson().toByteArray())
            }

            _stateFlowTabBagExport.update {
                it.copy(
                    exportStatus = ExportImportStatus.SUCCESS,
                )
            }
        }
    }

    fun resize(newResize: Float) {
        viewModelScope.launch {
            _stateFlowTabBag.update {
                it.copy(
                    resize = newResize
                )
            }

            TabSettingsModel.resize(repositorySettings, newResize)
        }
    }

    override fun markTabAsCurrentInSettings() {
        viewModelScope.launch {
            TabSettingsModel.markTabAsCurrentInSettings(repositorySettings, SettingsTab.TAB_DICE)
        }
    }
}
