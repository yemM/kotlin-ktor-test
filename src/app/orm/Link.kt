package com.weekando.app.orm

import org.jetbrains.exposed.sql.Table

object Link : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val label = text("label")
    val url = text("url")
}