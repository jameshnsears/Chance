package com.github.jameshnsears.chance.ui.dialog.dice.card.face

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.Stable
import coil.request.ImageRequest
import com.github.jameshnsears.chance.data.common.utility.svg.UtilitySvgSerializer
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.CardDiceSideEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.InputStream

@Stable
data class CardSideState(
    val sideNumber: Int,
    val sideNumberColour: String,
    val sideImageDrawableId: Int,
    val sideImageBase64: String,
    val sideImageRequest: ImageRequest?,
    val sideImageAvailable: Boolean,
    val sideDescription: String,
    val sideDescriptionColour: String,
    val sideApplyToAllNumberColour: Boolean,
    val sideApplyToAllDescription: Boolean,
    val sideApplyToAllSvg: Boolean,
    val diceSidesFewerThanSideNumber: Boolean,
    val svgImportInProgress: Boolean = false
)

enum class SvgImportError {
    NOT_A_SVG,
    TOO_BIG
}

class CardSideSvgImportException(svgImportError: SvgImportError) : Exception(svgImportError.name)


class CardSideService(
    private val application: Application,
    val side: Side
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val sideInitialState = CardSideState(
        sideNumber = side.number,
        sideNumberColour = side.numberColour,
        sideImageDrawableId = side.imageDrawableId,
        sideImageBase64 = side.imageBase64,
        sideImageRequest = null,
        sideImageAvailable = sideImageAvailableInit(),
        sideDescription = sideDescriptionInit(),
        sideDescriptionColour = side.descriptionColour,
        sideApplyToAllNumberColour = false,
        sideApplyToAllDescription = false,
        sideApplyToAllSvg = false,
        diceSidesFewerThanSideNumber = false,
        svgImportInProgress = false
    )

    private val _stateFlowCardSide = MutableStateFlow(sideInitialState)
    val stateFlowCardSide: StateFlow<CardSideState> = _stateFlowCardSide

    private var imageLoadingJob: Job? = null

    init {
        scope.launch(Dispatchers.IO) {
            CardDiceSideEvent.sharedFlowDiceSide.collect { itInt ->
                Timber.d("collect.CardDiceSideEvent=$itInt; side.number=${side.number}")

                val diceSidesFewer = itInt < side.number
                _stateFlowCardSide.update {
                    it.copy(diceSidesFewerThanSideNumber = diceSidesFewer)
                }
            }
        }

        imageLoadingJob = scope.launch {
            val imageRequest = UtilitySvgSerializer.imageRequestFromBase64StringAsync(
                application,
                side
            )
            _stateFlowCardSide.update {
                it.copy(sideImageRequest = imageRequest)
            }
        }

        Timber.d("card.side: side.uuid=${side.uuid}")
    }

    fun refresh(side: Side) {
        imageLoadingJob?.cancel()

        _stateFlowCardSide.update {
            it.copy(
                sideNumber = side.number,
                sideNumberColour = side.numberColour,
                sideImageDrawableId = side.imageDrawableId,
                sideImageBase64 = side.imageBase64,
                sideImageRequest = null,
                sideImageAvailable = (side.imageDrawableId != 0 || side.imageBase64.isNotEmpty()),
                sideDescription = side.description,
                sideDescriptionColour = side.descriptionColour,
                sideApplyToAllNumberColour = false,
                sideApplyToAllDescription = false,
                sideApplyToAllSvg = false,
                diceSidesFewerThanSideNumber = false,
                svgImportInProgress = false
            )
        }

        imageLoadingJob = scope.launch {
            val imageRequest = UtilitySvgSerializer.imageRequestFromBase64StringAsync(
                application,
                side
            )
            _stateFlowCardSide.update {
                it.copy(sideImageRequest = imageRequest)
            }
        }
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

    suspend fun sideImageSvgImport(uri: Uri) = withContext(Dispatchers.IO) {
        application.contentResolver.openInputStream(uri)?.use { inputStream ->
            sideImageSvgImport(inputStream)
        }
    }

    private fun isSvgTooBig(candidateSvgString: String, kiloBytes: Int) {
        if (candidateSvgString.length > kiloBytes * 1024)
            throw CardSideSvgImportException(SvgImportError.TOO_BIG)
    }

    suspend fun sideImageSvgImport(inputStream: InputStream, kiloBytes: Int = 256) =
        withContext(Dispatchers.IO) {
            val candidateSvgString = sideImageSvgImportReadFile(inputStream)

            isSvgTooBig(candidateSvgString, kiloBytes)

            if (!UtilitySvgSerializer.isStringSvg(candidateSvgString)) {
                throw CardSideSvgImportException(SvgImportError.NOT_A_SVG)
            }

            _stateFlowCardSide.update { it.copy(svgImportInProgress = true) }

            try {
                val encodedBase64 = UtilitySvgSerializer.encodeIntoBase64StringAsync(candidateSvgString)
                val imageRequest = UtilitySvgSerializer.imageRequestFromSvgStringAsync(
                    application,
                    candidateSvgString
                )

                _stateFlowCardSide.update {
                    it.copy(
                        sideImageBase64 = encodedBase64,
                        sideImageRequest = imageRequest,
                        sideImageDrawableId = 0,
                        svgImportInProgress = false
                    )
                }

                Timber.d("sideImageSvgImport completed successfully")
            } catch (e: Exception) {
                Timber.e(e, "sideImageSvgImport failed")
                _stateFlowCardSide.update { it.copy(svgImportInProgress = false) }
            }
        }

    private fun sideImageSvgImportReadFile(inputStream: InputStream): String {
        return inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
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
                sideImageRequest = null
            )
        }
    }

    private fun sideImageAvailableInit(): Boolean =
        (side.imageDrawableId != 0 || side.imageBase64.isNotEmpty())

    fun sideImageAvailable(): Boolean =
        (_stateFlowCardSide.value.sideImageDrawableId != 0 || _stateFlowCardSide.value.sideImageBase64.isNotEmpty())
}
