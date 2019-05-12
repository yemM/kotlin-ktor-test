package com.weekando.app.orm.pool

import javax.sql.DataSource

interface ConnectionPool {
    fun create(): DataSource
}