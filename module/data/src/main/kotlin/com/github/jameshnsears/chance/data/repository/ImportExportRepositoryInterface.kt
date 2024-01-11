package com.github.jameshnsears.chance.data.repository

interface ImportExportRepositoryInterface {
    suspend fun export(): String
    suspend fun import(json: String)
}
