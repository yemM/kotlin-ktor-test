package com.weekando.app.container

import com.weekando.app.orm.pool.ConnectionPool
import com.weekando.app.orm.pool.ConnectionPoolImpl
import com.weekando.link.repository.LinkRepository
import com.weekando.link.repository.LinkRepositoryImpl
import org.koin.dsl.module

val ORMModule = module {
    single<LinkRepository> {
        LinkRepositoryImpl(
            pool = get()
        )
    }
    single<ConnectionPool> {
        ConnectionPoolImpl(
            driver = getProperty("orm.driver"),
            dbUrl = getProperty("orm.db_url"),
            maxPoolSize = getProperty("orm.max_pool_size")
        )
    }
}