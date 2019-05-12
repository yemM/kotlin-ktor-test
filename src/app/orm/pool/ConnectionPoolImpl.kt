package com.weekando.app.orm.pool

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource


class ConnectionPoolImpl(val driver: String, val dbUrl: String, val maxPoolSize: Int): ConnectionPool {
    override fun create(): DataSource {
        val config = HikariConfig()
        config.driverClassName = driver
        config.jdbcUrl = dbUrl
        config.maximumPoolSize = maxPoolSize
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }
}