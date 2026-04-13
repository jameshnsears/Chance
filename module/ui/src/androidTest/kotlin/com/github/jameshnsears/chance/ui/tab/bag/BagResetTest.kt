package com.github.jameshnsears.chance.ui.tab.bag

import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import org.junit.Test

class BagResetTest : AndroidTestHelper() {

    @Test
    fun confirmReset() {
        reset()
        /*
    confirm that dice bag contains three dice
     */
    }

    private fun reset() {
        /*
            click "Dice" tab

    click "Reset Storage" button

    click "OK" on confirmation dialog
         */
    }

    ////////////////////

    @Test
    fun deleteDiceThenResetStorage() {
        /*
    click Mr Benn dice, the knight

    click "Delete" button

    click "OK" on confirmation dialog

    confirm that dice bag contains two dice

    confirm dice bag does not contain mr Benn dice

    confirm dice bag contains only d6; and The Dice Man dice
     */

        reset()
    }
}
