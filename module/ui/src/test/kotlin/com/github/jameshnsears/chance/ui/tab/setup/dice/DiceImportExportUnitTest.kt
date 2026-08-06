package com.github.jameshnsears.chance.ui.tab.setup.dice

import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.repo.api.RepositoryImportStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class DiceImportExportUnitTest {
    @Test
    fun toStringResId() {
        assertEquals(
            R.string.tab_bag_import_failure_json_file_empty,
            RepositoryImportStatus.JSON_FILE_EMPTY.toStringResId()
        )
        assertEquals(
            R.string.tab_bag_import_failure_json_file_unknown_version,
            RepositoryImportStatus.JSON_FILE_UNKNOWN_VERSION.toStringResId()
        )
        assertEquals(
            R.string.tab_bag_import_failure_json_file_missing_section,
            RepositoryImportStatus.JSON_FILE_MISSING_SECTION.toStringResId()
        )
        assertEquals(
            R.string.tab_bag_import_failure_json_dice_missing,
            RepositoryImportStatus.JSON_DICE_MISSING.toStringResId()
        )
        assertEquals(
            R.string.tab_bag_import_failure_json_dice_uuid,
            RepositoryImportStatus.JSON_DICE_UUID.toStringResId()
        )
        assertEquals(
            R.string.tab_bag_import_failure_json_dice_title,
            RepositoryImportStatus.JSON_DICE_TITLE.toStringResId()
        )
        assertEquals(
            R.string.tab_bag_import_failure_json_side_size,
            RepositoryImportStatus.JSON_SIDE_SIZE.toStringResId()
        )
        assertEquals(
            R.string.tab_bag_import_failure_json_schema_settings,
            RepositoryImportStatus.JSON_SCHEMA_SETTINGS.toStringResId()
        )
        assertEquals(
            R.string.tab_bag_import_failure_json_schema_dice,
            RepositoryImportStatus.JSON_SCHEMA_DICE.toStringResId()
        )
        assertEquals(
            R.string.tab_bag_import_failure_json_schema_side,
            RepositoryImportStatus.JSON_SCHEMA_SIDE.toStringResId()
        )
        assertEquals(
            R.string.tab_bag_import_failure_json_schema_group,
            RepositoryImportStatus.JSON_SCHEMA_GROUP.toStringResId()
        )
        assertEquals(
            R.string.tab_bag_import_failure,
            RepositoryImportStatus.SUCCESS.toStringResId()
        )
    }
}
