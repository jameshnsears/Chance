package com.github.jameshnsears.chance.data.repo.api

class RepositoryImportException(
    val detail: RepositoryImportStatus
) : Exception()

enum class RepositoryImportStatus {
    SUCCESS,
    JSON_FILE_EMPTY,
    JSON_FILE_UNKNOWN_VERSION,
    JSON_FILE_MISSING_SECTION,
    JSON_DICE_MISSING,
    JSON_DICE_UUID,
    JSON_DICE_TITLE,
    JSON_SIDE_SIZE,
    JSON_SCHEMA_SETTINGS,
    JSON_SCHEMA_DICE,
    JSON_SCHEMA_SIDE,
    JSON_SCHEMA_GROUP
}
