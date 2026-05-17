package com.github.jameshnsears.chance.data.repo.api

import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryImportExceptionUnitTest {
    @Test
    fun repositoryImportException() {
        val status = RepositoryImportStatus.ERROR_IMPORT_EMPTY
        val exception = RepositoryImportException(status)
        assertEquals(status, exception.detail)
    }
}
