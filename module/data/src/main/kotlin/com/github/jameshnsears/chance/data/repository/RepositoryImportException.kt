package com.github.jameshnsears.chance.data.repository

class RepositoryImportException(
    val detail: RepositoryImportStatus
) : Exception()

enum class RepositoryImportStatus {
    NONE,
    ERROR_IMPORT_EMPTY,
    ERROR_SECTION_MISSING,
    ERROR_DICE_MISSING,
    ERROR_DICE_UNKNOWN,
    ERROR_DICE_TITLE,
    ERROR_SIDE_SIZE,
    ERROR_SCHEMA_SETTINGS,
    ERROR_SCHEMA_DICE,
    ERROR_SCHEMA_SIDE,
}
