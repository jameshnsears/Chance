package com.github.jameshnsears.chance.ui.tab.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.data.repository.ImportValidation
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryInterface
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryInterface
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class TabBagViewModel(
    val settingsRepository: SettingsRepositoryInterface,
    val bagRepository: DiceBagRepositoryInterface,
    val rollRepository: RollRepositoryInterface,
) : ViewModel() {
    suspend fun export(): String {
        return "[" +
                settingsRepository.export() +
                "," +
                bagRepository.export() +
                "," +
                rollRepository.export() +
                "]"
    }

    private var _importFailed = MutableStateFlow(false)
    var importFailed: StateFlow<Boolean> = _importFailed

    suspend fun import(json: String) {
        viewModelScope.launch {
            try {
                val rootNode = jacksonObjectMapper().readTree(json)

                ImportValidation.validate(rootNode)

                settingsRepository.import(rootNode.get(0).toString())
                bagRepository.import(rootNode.get(1).toString())
                rollRepository.import(rootNode.get(2).toString())

            } catch (e: Exception) {
                Timber.d(e.message)
                _importFailed.value = true
            }
        }
    }
}
