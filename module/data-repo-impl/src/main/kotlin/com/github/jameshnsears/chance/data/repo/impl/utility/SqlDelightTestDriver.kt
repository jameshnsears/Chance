package com.github.jameshnsears.chance.data.repo.impl.utility

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.ChanceDatabase
import java.util.Properties

object SqlDelightTestDriver {
    fun create(): SqlDriver {
        // Force the use of JDBC driver for unit tests which are run on the JVM (Robolectric or standard JUnit)
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY, Properties().apply {
            put("foreign_keys", "true")
        })

        ChanceDatabase.Schema.create(driver)

        // The JDBC in-memory driver uses a single shared java.sql.Connection which is not
        // thread-safe; repository flows execute queries on Dispatchers.IO concurrently. Wrap the
        // driver so all statements (and whole transactions) are serialized.
        return SynchronizedSqlDriver(driver)
    }
}
