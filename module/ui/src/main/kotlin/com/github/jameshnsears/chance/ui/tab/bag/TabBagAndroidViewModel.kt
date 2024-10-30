package com.github.jameshnsears.chance.ui.tab.bag

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.data.repository.RepositoryImportException
import com.github.jameshnsears.chance.data.repository.RepositoryImportStatus
import com.github.jameshnsears.chance.data.repository.RepositoryImportValidation
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.tab.ResizeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

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
    resizeInitialValue: Int,
) : AndroidViewModel(application) {
    private val _stateFlowResize = MutableStateFlow(resizeInitialValue)
    val stateFlowResize: StateFlow<Int> = _stateFlowResize

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
                repositorySettings.jsonExport() +
                "," +
                repositoryBag.jsonExport() +
                "," +
                repositoryRoll.jsonExport() +
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
                val rootNode = jacksonObjectMapper().readTree(json)     // doesn't work in API 24

                RepositoryImportValidation.validate(rootNode)

                Timber.d("import.validation completed")

                repositorySettings.clear()
                repositorySettings.jsonImport(rootNode.get(0).toString())

                repositoryBag.clear()
                repositoryBag.jsonImport(rootNode.get(1).toString())

                repositoryRoll.clear()
                repositoryRoll.jsonImport(rootNode.get(2).toString())

                _stateFlowTabBagImport.update {
                    it.copy(
                        importStatus = ExportImportStatus.SUCCESS,
                        importDetail = RepositoryImportStatus.NONE
                    )
                }

                _stateFlowResize.value = repositorySettings.fetch().first().resize

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
            } catch (e: Exception) {
                Timber.e(e.message.toString())

                _stateFlowTabBagImport.update {
                    it.copy(
                        importStatus = ExportImportStatus.FAILURE,
                        importDetail = RepositoryImportStatus.ERROR_PROTO
                    )
                }

                Timber.e("import.completed.failure")
            }
        }
    }

    fun importFileJson(uri: Uri) {
        viewModelScope.launch {
            _stateFlowTabBagImport.update {
                it.copy(
                    importStatus = ExportImportStatus.IMPORT_STARTED
                )
            }

            getContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                import(inputStream.reader().readText())
                inputStream.close()
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

    fun resizeSettings(newResize: Int) {
        viewModelScope.launch {
            if (_stateFlowResize.value != newResize) {
                _stateFlowResize.value = newResize

                val settings = repositorySettings.fetch().first()
                settings.resize = newResize
                repositorySettings.store(settings)

                ResizeEvent.emit()
            }
        }
    }
}
