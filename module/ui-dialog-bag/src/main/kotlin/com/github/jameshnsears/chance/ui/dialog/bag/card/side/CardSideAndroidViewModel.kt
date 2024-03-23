package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import android.app.Application
import android.net.Uri
import androidx.lifecycle.viewModelScope
import coil.request.ImageRequest
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.domain.utility.svg.UtilitySvgSerializer
import com.github.jameshnsears.chance.ui.dialog.bag.card.CardAndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStream

data class CardSideState(
    val sideNumber: Int,
    var sideNumberColour: String,
    var sideDescription: String,
    var sideDescriptionColour: String,
    var sideImageImageRequest: ImageRequest?,
    var sideImageBase64: String,
)

class CardSideSvgImportException : Exception()

class CardSideAndroidViewModel(
    application: Application,
    val side: Side
) : CardAndroidViewModel(application) {
    private val sideInitialState = CardSideState(
        side.number,
        side.numberColour,
        sideDescriptionInit(),
        side.descriptionColour,
        UtilitySvgSerializer.imageRequestFromBase64String(getApplication(), side.imageBase64),
        side.imageBase64
    )

    private val _stateFlow = MutableStateFlow(sideInitialState)
    val stateFlowCardSide: StateFlow<CardSideState> = _stateFlow

    fun sideNumberColour(colour: String) {
        _stateFlow.update { it.copy(sideNumberColour = colour) }
    }

    private fun sideDescriptionInit(): String {
        return if (side.description != "")
            side.description
        else if (side.descriptionStringsId != 0)
            getString(side.descriptionStringsId)
        else
            ""
    }

    fun sideDescription(description: String) {
        _stateFlow.update { it.copy(sideDescription = description) }
    }

    fun sideDescriptionColour(colour: String) {
        _stateFlow.update { it.copy(sideDescriptionColour = colour) }
    }

    fun sideImageSvgImport(uri: Uri)  =
        sideImageSvgImport(getApplication<Application>().contentResolver.openInputStream(uri))

    fun sideImageSvgImport(inputStream: InputStream?) {
        val candidateSvgString = readCandidateSvgString(inputStream)

        if (UtilitySvgSerializer.isStringSvg(candidateSvgString)) {
            _stateFlow.update {
                it.copy(
                    sideImageImageRequest = UtilitySvgSerializer.imageRequestFromSvgString(
                        getApplication(),
                        candidateSvgString
                    ),
                    sideImageBase64 = UtilitySvgSerializer.encodeIntoBase64String(
                        candidateSvgString
                    )
                )
            }
        } else {
            throw CardSideSvgImportException()
        }
    }

    private fun readCandidateSvgString(inputStream: InputStream?): String {
        var possibleSvgString: String
        inputStream.use {
            val reader = it?.bufferedReader(Charsets.UTF_8)
            possibleSvgString = reader!!.readText()
        }

        return possibleSvgString
    }
}
