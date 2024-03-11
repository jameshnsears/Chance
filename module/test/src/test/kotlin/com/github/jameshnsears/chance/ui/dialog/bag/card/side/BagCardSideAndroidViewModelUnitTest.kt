package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertNotNull

class BagCardSideAndroidViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun sideCardModify() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()
        val side = SampleBag.d2.sides[0]
        assertStateSide(dialogBagAndroidViewModel, side)

        val bagCardSideAndroidViewModel =
            dialogBagAndroidViewModel.bagCardSideAndroidViewModel
        val sideChanged = side.copy(
            numberColour = "FF00FF00",
            description = "d2 Side # 2",
            descriptionColour = "00FF00FF"
        )
        bagCardSideAndroidViewModel.sideNumberColour(sideChanged.numberColour)
        bagCardSideAndroidViewModel.sideDescription(sideChanged.description)
        bagCardSideAndroidViewModel.sideDescriptionColour(sideChanged.descriptionColour)
        assertStateSide(dialogBagAndroidViewModel, sideChanged)

        bagCardSideAndroidViewModel.sideReset()
        assertStateSide(dialogBagAndroidViewModel, side)
    }

    private fun assertStateSide(
        dialogBagAndroidViewModel: DialogBagAndroidViewModel,
        side: Side
    ) {
        val stateFlowSide =
            dialogBagAndroidViewModel.bagCardSideAndroidViewModel.stateFlowSide.value

        assertEquals(side.number, stateFlowSide.sideNumber)
        assertEquals(side.numberColour, stateFlowSide.sideNumberColour)
        assertEquals(side.description, stateFlowSide.sideDescription)
        assertEquals(side.descriptionColour, stateFlowSide.sideDescriptionColour)
        assertNotNull(stateFlowSide.sideImageSVG.data)
    }
}
