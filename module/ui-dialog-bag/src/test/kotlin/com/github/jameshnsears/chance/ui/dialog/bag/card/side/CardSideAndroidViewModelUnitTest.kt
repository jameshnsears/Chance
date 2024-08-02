package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import kotlin.test.assertNotNull


class CardSideAndroidViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun sideCardSideDescriptionInit() = runTest {
        val diceInDialogBag = BagDataTestDouble().d2
        val sideInDialogBag = diceInDialogBag.sides[0]

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, sideInDialogBag
        )

        val bagCardSideAndroidViewModel =
            dialogBagAndroidViewModel.cardSideAndroidViewModel

        assertEquals(
            diceInDialogBag.sides[0].description,
            bagCardSideAndroidViewModel.stateFlowCardSide.value.sideDescription
        )
    }

    @Test
    fun sideCardModifyAndReset() = runTest {
        val diceInDialogBag = BagDataTestDouble().d2
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
            javaClass.classLoader?.getResourceAsStream("data/svg/MrBenn/lion.svg")
        )
    }

    @Test
    fun sideCardSvgClear() {
        val diceInDialogBag = BagDataTestDouble().d6
        val sideInDialogBag = diceInDialogBag.sides[0]

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, sideInDialogBag
        )

        val bagCardSideAndroidViewModel =
            dialogBagAndroidViewModel.cardSideAndroidViewModel

        assertTrue(bagCardSideAndroidViewModel.sideImageAvailable())

        bagCardSideAndroidViewModel.sideImageSvgClear()

        assertFalse(bagCardSideAndroidViewModel.sideImageAvailable())
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
        assertNotNull(stateFlowSide.sideImageRequest?.data)
        assertEquals(side.imageBase64, stateFlowSide.sideImageBase64)
    }
}
