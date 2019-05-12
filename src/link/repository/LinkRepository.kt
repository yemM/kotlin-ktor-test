package com.weekando.link.repository

import com.weekando.link.model.Link

interface LinkRepository {
    fun get(id: Int): Link?

    fun add(link: Link)
}