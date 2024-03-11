package com.github.jameshnsears.chance.data.repository

interface RepositoryImportExportInterface {
    suspend fun exportJson(): String
    suspend fun importJson(json: String)
}
