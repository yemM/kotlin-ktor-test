package com.weekando.link.repository

import com.weekando.app.orm.Link as LinkTable
import com.weekando.app.orm.pool.ConnectionPool
import com.weekando.link.model.Link as LinkModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class LinkRepositoryImpl(val pool: ConnectionPool): LinkRepository {
    override fun get(id: Int): LinkModel? {

        var link: LinkModel? = null

        transaction {
            val query: Query = LinkTable.select {
                LinkTable.id.eq(id)
            }

            if(1 <= query.count()) {
                val row = query.first()
                link = LinkModel(
                    id = row[LinkTable.id],
                    label = row[LinkTable.label],
                    url = row[LinkTable.url]
                )
            }
        }

        return link
    }

    override fun add(link: LinkModel) {
        Database.connect(pool.create())

        transaction {
            LinkTable.insert {
                it[label] = link.label
                it[url] = link.url
            }
        }
    }
}