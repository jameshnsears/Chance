package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.request.ImageRequest
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.utility.svg.UtilitySvgSerializer
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.CardDiceSideEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.InputStream

data class CardSideState(
    val sideNumber: Int,
    var sideNumberColour: String,
    var sideImageDrawableId: Int,
    var sideImageBase64: String,
    var sideImageBase64Request: ImageRequest?,
    var sideImageAvailable: Boolean,
    var sideDescription: String,
    var sideDescriptionColour: String,
    var sideApplyToAllNumberColour: Boolean,
    var sideApplyToAllDescription: Boolean,
    var sideApplyToAllSvg: Boolean,
    var diceSidesFewerThanSdeNumber: Boolean
)

class CardSideSvgImportException : Exception()

class CardSideAndroidViewModel(
    application: Application,
    val side: Side
) : AndroidViewModel(application) {
    private val sideInitialState = CardSideState(
        sideNumber = side.number,
        sideNumberColour = side.numberColour,
        sideImageDrawableId = side.imageDrawableId,
        sideImageBase64 = side.imageBase64,
        sideImageBase64Request = UtilitySvgSerializer.imageRequestFromBase64String(
            getApplication(),
            side.imageBase64
        ),
        sideImageAvailable = sideImageAvailableInit(),
        sideDescription = sideDescriptionInit(),
        sideDescriptionColour = side.descriptionColour,
        sideApplyToAllNumberColour = false,
        sideApplyToAllDescription = false,
        sideApplyToAllSvg = false,
        diceSidesFewerThanSdeNumber = false
    )

    private val _stateFlowCardSide = MutableStateFlow(sideInitialState)
    val stateFlowCardSide: StateFlow<CardSideState> = _stateFlowCardSide

    init {
        viewModelScope.launch {
            CardDiceSideEvent.sharedFlowDiceSide.collect { itInt ->
                Timber.d("collect=$itInt; side.number=$side.number")

                val diceSidesFewer = itInt < side.number
                _stateFlowCardSide.update {
                    it.copy(diceSidesFewerThanSdeNumber = diceSidesFewer)
                }
            }
        }

        Timber.d("card.side: side.uuid=${side.uuid}")

    }

    fun sideNumberColour(colour: String) {
        _stateFlowCardSide.update { it.copy(sideNumberColour = colour) }
    }

    private fun sideDescriptionInit() = side.description

    fun sideDescription(description: String) {
        _stateFlowCardSide.update { it.copy(sideDescription = description) }
    }

    fun sideDescriptionColour(colour: String) {
        _stateFlowCardSide.update { it.copy(sideDescriptionColour = colour) }
    }

    fun sideImageSvgImport(uri: Uri) =
        sideImageSvgImport(getApplication<Application>().contentResolver.openInputStream(uri))

    fun sideImageSvgImport(inputStream: InputStream?) {
        val candidateSvgString = sideImageSvgImportReadFile(inputStream)

        if (UtilitySvgSerializer.isStringSvg(candidateSvgString)) {
            _stateFlowCardSide.update {
                it.copy(
                    sideImageBase64 = UtilitySvgSerializer.encodeIntoBase64String(
                        candidateSvgString
                    ),
                    sideImageBase64Request = UtilitySvgSerializer.imageRequestFromSvgString(
                        getApplication(),
                        candidateSvgString
                    ),
                )
            }
        } else {
            throw CardSideSvgImportException()
        }
    }

    private fun sideImageSvgImportReadFile(inputStream: InputStream?): String {
        var fileContents: String

        inputStream.use {
            val reader = it?.bufferedReader(Charsets.UTF_8)
            fileContents = reader!!.readText()
        }

        return fileContents
    }

    fun sideApplyToAllNumberColour(switch: Boolean) {
        Timber.d("sideApplyToAllNumberColour=$switch")
        _stateFlowCardSide.update { it.copy(sideApplyToAllNumberColour = switch) }
    }

    fun sideApplyToAllDescription(switch: Boolean) {
        Timber.d("sideApplyToAllDescription=$switch")
        _stateFlowCardSide.update { it.copy(sideApplyToAllDescription = switch) }
    }

    fun sideApplyToAllSvg(switch: Boolean) {
        Timber.d("sideApplyToAllSvg=$switch")
        _stateFlowCardSide.update { it.copy(sideApplyToAllSvg = switch) }
    }

    fun sideImageSvgClear() {
        _stateFlowCardSide.update {
            it.copy(
                sideImageDrawableId = 0,
                sideImageBase64 = "",
                sideImageBase64Request = null
            )
        }
    }

    private fun sideImageAvailableInit(): Boolean =
        (side.imageDrawableId != 0 || side.imageBase64 != "")

    fun sideImageAvailable(): Boolean =
        (_stateFlowCardSide.value.sideImageDrawableId != 0 || _stateFlowCardSide.value.sideImageBase64 != "")
}
