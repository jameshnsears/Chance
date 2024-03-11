package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import android.app.Application
import android.net.Uri
import androidx.lifecycle.viewModelScope
import coil.request.ImageRequest
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.domain.utility.svg.UtilitySvgSerializer
import com.github.jameshnsears.chance.ui.dialog.bag.card.BagCardAndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

data class SideInitialState(
    val sideNumber: Int,
    var sideNumberColour: String,
    var sideDescription: String,
    var sideDescriptionColour: String,
    var sideImageSVG: ImageRequest,
)

class BagCardSideAndroidViewModel(
    application: Application,
    val side: Side
) : BagCardAndroidViewModel(application) {
    private val sideInitialState = SideInitialState(
        side.number,
        side.numberColour,
        sideDescriptionInit(),
        side.descriptionColour,
        sideImageSVGInit()
    )

    private val _stateFlow = MutableStateFlow(sideInitialState)
    val stateFlowSide: StateFlow<SideInitialState> = _stateFlow

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

    private fun sideImageSVGInit() =
        UtilitySvgSerializer.decodeIntoImageRequest(getApplication(), side.imageBase64)

    fun sideImageSVGImport(uri: Uri) {
        Timber.d("${uri.path}")
//        context.contentResolver.openInputStream(uri)?.use { inputStream ->
//            val text = inputStream.reader().readText()
//        }
    }

    fun sideReset() {
        viewModelScope.launch {
            _stateFlow.update {
                it.copy(
                    sideNumber = sideInitialState.sideNumber,
                    sideNumberColour = sideInitialState.sideNumberColour,
                    sideDescription = sideInitialState.sideDescription,
                    sideDescriptionColour = sideInitialState.sideDescriptionColour,
                    sideImageSVG = sideInitialState.sideImageSVG
                )
            }
        }
    }
}
