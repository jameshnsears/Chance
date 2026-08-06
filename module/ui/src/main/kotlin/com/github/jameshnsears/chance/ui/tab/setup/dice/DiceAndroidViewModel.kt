package com.github.jameshnsears.chance.ui.tab.setup.dice

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.repo.api.RepositoryImportException
import com.github.jameshnsears.chance.data.repo.api.RepositoryImportStatus
import com.github.jameshnsears.chance.data.repo.api.RepositoryImportValidation
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.BuildConfig
import com.github.jameshnsears.chance.ui.tab.SetupImportEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

enum class ExportImportStatus {
    READY,
    IMPORT_STARTED,
    SUCCESS,
    FAILURE
}

data class TabBagExportState(
    val exportStatus: ExportImportStatus,
)

data class TabBagImportState(
    val importStatus: ExportImportStatus,
    val importDetail: RepositoryImportStatus
)

class DiceAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
    val repositoryGroup: RepositoryGroupInterface,
    resizeInitialValue: Float,
) : AndroidViewModel(application) {
    val validator = RepositoryImportValidation()

    val stateFlowResize: StateFlow<Float> = flow {
        emitAll(repositorySettings.fetch())
    }
        .map { it.resizeZoom }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = resizeInitialValue
        )

    fun resetExportImportStatus() {
        viewModelScope.launch {
            _stateFlowTabBagExport.update {
                it.copy(
                    exportStatus = ExportImportStatus.READY,
                )
            }

            _stateFlowTabBagImport.update {
                it.copy(
                    importStatus = ExportImportStatus.READY,
                    importDetail = RepositoryImportStatus.SUCCESS
                )
            }
        }
    }

    suspend fun exportRepositoriesAsJson(): String = withContext(Dispatchers.IO) {
        val mapper = jacksonObjectMapper()
        val rootNode = mapper.createObjectNode()
        rootNode.put("version", BuildConfig.VERSION)
        rootNode.set<JsonNode>("settings", mapper.readTree(repositorySettings.jsonExport()))
        rootNode.set<JsonNode>("bag", mapper.readTree(repositoryBag.jsonExport()))
        rootNode.set<JsonNode>("rolls", mapper.readTree(repositoryRoll.jsonExport()))
        rootNode.set<JsonNode>("groups", mapper.readTree(repositoryGroup.jsonExport()))
        rootNode.toString()
    }

    private val _stateFlowTabBagExport = MutableStateFlow(
        TabBagExportState(
            exportStatus = ExportImportStatus.READY,
        )
    )
    val stateFlowTabBagExport: StateFlow<TabBagExportState> = _stateFlowTabBagExport

    private val _stateFlowTabBagImport = MutableStateFlow(
        TabBagImportState(
            importStatus = ExportImportStatus.READY,
            importDetail = RepositoryImportStatus.SUCCESS
        )
    )
    val stateFlowTabBagImport: StateFlow<TabBagImportState> = _stateFlowTabBagImport

    suspend fun import(json: String) = withContext(Dispatchers.IO) {
        Timber.d("import.started")

        try {
            if (json.isBlank()) {
                throw RepositoryImportException(RepositoryImportStatus.JSON_FILE_EMPTY)
            }

            val rootNode = try {
                jacksonObjectMapper().readTree(json)
            } catch (_: Exception) {
                throw RepositoryImportException(RepositoryImportStatus.JSON_FILE_MISSING_SECTION)
            }

            val importData = validator.validate(rootNode)

            Timber.d("import.validation completed. version=${importData.version}")

            repositorySettings.clear()
            repositoryBag.clear()
            repositoryRoll.clear()

            repositorySettings.jsonImport(importData.jsonSettings.toString())

            if (validator.jsonVersion == "2.5.0") {
                repositoryBag.jsonImport(importData.jsonBag.toString())
                repositoryRoll.jsonImport(importData.jsonRolls.toString())
                repositoryGroup.jsonImport(importData.jsonGroups.toString())
            } else {
                repositoryBag.jsonImport(importData.jsonBag.toString())
                repositoryRoll.jsonImport(importData.jsonRolls.toString())

                val jsonGroups = importData.jsonGroups
                if (jsonGroups != null && !jsonGroups.isEmpty && !jsonGroups.isNull)
                    repositoryGroup.jsonImport(jsonGroups.toString())
            }

            _stateFlowTabBagImport.update {
                it.copy(
                    importStatus = ExportImportStatus.SUCCESS,
                    importDetail = RepositoryImportStatus.SUCCESS
                )
            }

            SetupImportEvent.emit()

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
            Timber.e(e)

            _stateFlowTabBagImport.update {
                it.copy(
                    importStatus = ExportImportStatus.FAILURE,
                    importDetail = RepositoryImportStatus.JSON_FILE_MISSING_SECTION
                )
            }

            Timber.e("import.completed.failure.generic")
        }
    }

    fun importFileJson(uri: Uri) {
        viewModelScope.launch {
            _stateFlowTabBagImport.update {
                it.copy(
                    importStatus = ExportImportStatus.IMPORT_STARTED
                )
            }

            try {
                val json = withContext(Dispatchers.IO) {
                    getApplication<Application>().contentResolver.openInputStream(uri)?.use { inputStream ->
                        inputStream.reader().readText()
                    }
                }

                if (json != null) {
                    import(json)
                } else {
                    _stateFlowTabBagImport.update {
                        it.copy(
                            importStatus = ExportImportStatus.FAILURE,
                            importDetail = RepositoryImportStatus.JSON_FILE_MISSING_SECTION
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                _stateFlowTabBagImport.update {
                    it.copy(
                        importStatus = ExportImportStatus.FAILURE,
                        importDetail = RepositoryImportStatus.JSON_FILE_MISSING_SECTION
                    )
                }
            }
        }
    }

    fun exportFileJson(uri: Uri) {
        viewModelScope.launch {
            val json = exportRepositoriesAsJson()
            withContext(Dispatchers.IO) {
                getApplication<Application>().contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(json.toByteArray())
                }
            }

            _stateFlowTabBagExport.update {
                it.copy(
                    exportStatus = ExportImportStatus.SUCCESS,
                )
            }
        }
    }

    fun resizeSettings(newResize: Float) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            if (settings.resizeZoom != newResize) {
                settings.resizeZoom = newResize
                repositorySettings.store(settings)

                DiceResizeEvent.emit()
            }
        }
    }

    fun resetStorage() {
        viewModelScope.launch {
            RepositoryFactory(getApplication<Application>()).resetStorage()

            DiceResetEvent.emit()
        }
    }
}
