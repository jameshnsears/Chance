package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import kotlin.test.assertNotNull


class CardSideAndroidViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun sideCardModifyAndReset() = runTest {
        val diceInDialogBag = SampleBag.d2
        val sideInDialogBag = diceInDialogBag.sides[0]

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, sideInDialogBag
        )
        assertStateSide(dialogBagAndroidViewModel, sideInDialogBag)

        val bagCardSideAndroidViewModel =
            dialogBagAndroidViewModel.cardSideAndroidViewModel

        val sideChanged = sideInDialogBag.copy(
            numberColour = "00000000",
            description = "updatedDescription",
            descriptionColour = "FFFFFFFF"
        )
        bagCardSideAndroidViewModel.sideNumberColour(sideChanged.numberColour)
        bagCardSideAndroidViewModel.sideDescription(sideChanged.description)
        bagCardSideAndroidViewModel.sideDescriptionColour(sideChanged.descriptionColour)
        assertStateSide(dialogBagAndroidViewModel, sideChanged)
    }

    @Test
    fun sideCardSvgFileImportInvalid() {
        try {
            getDialogBagAndroidViewModel().cardSideAndroidViewModel.sideImageSvgImport(
                javaClass.classLoader?.getResourceAsStream("data/svg/empty.svg")
            )
            fail()
        } catch (cardSideSvgImportException: CardSideSvgImportException) {
            // expected
        }
    }

    @Test
    fun sideCardSvgFileImportValid() {
        getDialogBagAndroidViewModel().cardSideAndroidViewModel.sideImageSvgImport(
            javaClass.classLoader?.getResourceAsStream("data/svg/MrBenn/centurion.svg")
        )
    }

    private fun assertStateSide(
        dialogBagAndroidViewModel: DialogBagAndroidViewModel,
        side: Side
    ) {
        val stateFlowSide =
            dialogBagAndroidViewModel.cardSideAndroidViewModel.stateFlowCardSide.value

        assertEquals(side.number, stateFlowSide.sideNumber)
        assertEquals(side.numberColour, stateFlowSide.sideNumberColour)
        assertEquals(side.description, stateFlowSide.sideDescription)
        assertEquals(side.descriptionColour, stateFlowSide.sideDescriptionColour)
        assertNotNull(stateFlowSide.sideImageImageRequest?.data)
        assertEquals(side.imageBase64, stateFlowSide.sideImageBase64)
    }
}
