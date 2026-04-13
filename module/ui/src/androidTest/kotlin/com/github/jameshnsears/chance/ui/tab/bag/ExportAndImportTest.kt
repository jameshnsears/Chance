package com.github.jameshnsears.chance.ui.tab.bag

import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import org.junit.Test

class ExportAndImportTest : AndroidTestHelper() {
    @Test
    fun exportDeleteDiceThenImport() {
        /*
        click "Dice" tab

        click "Export" button

        rename file to "test_export.json"

        click "Save" button

        click #5 in d6

        click "Delete" button

        click "OK" on confirmation dialog

        confirm that dice bag contains two dice

        confirm dice bag does not contain d6

        click "Import" button

        select "test_export.json"

        confirm that dice bag contains three dice
         */
    }
}
