package com.github.jameshnsears.chance.data.repo.impl.utility

import app.cash.sqldelight.Query
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class SynchronizedSqlDriver(private val delegate: SqlDriver) : SqlDriver {
    private val lock = ReentrantLock()

    override fun <R> executeQuery(
        identifier: Int?,
        sql: String,
        mapper: (SqlCursor) -> QueryResult<R>,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?,
    ): QueryResult<R> = lock.withLock {
        delegate.executeQuery(identifier, sql, mapper, parameters, binders)
    }

    override fun execute(
        identifier: Int?,
        sql: String,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?,
    ): QueryResult<Long> = lock.withLock {
        delegate.execute(identifier, sql, parameters, binders)
    }

    override fun newTransaction(): QueryResult<Transacter.Transaction> = lock.withLock {
        delegate.newTransaction()
    }

    override fun currentTransaction(): Transacter.Transaction? = delegate.currentTransaction()

    override fun addListener(vararg queryKeys: String, listener: Query.Listener) {
        delegate.addListener(*queryKeys, listener = listener)
    }

    override fun removeListener(vararg queryKeys: String, listener: Query.Listener) {
        delegate.removeListener(*queryKeys, listener = listener)
    }

    override fun notifyListeners(vararg queryKeys: String) {
        lock.withLock {
            delegate.notifyListeners(*queryKeys)
        }
    }

    override fun close() {
        lock.withLock {
            delegate.close()
        }
    }
}
