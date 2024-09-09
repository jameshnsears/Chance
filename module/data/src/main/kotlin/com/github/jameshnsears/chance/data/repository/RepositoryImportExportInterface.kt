package com.github.jameshnsears.chance.data.repository

interface RepositoryImportExportInterface {
    suspend fun clear()
    suspend fun jsonExport(): String
    suspend fun jsonImport(json: String)
}
