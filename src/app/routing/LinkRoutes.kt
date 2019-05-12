package com.weekando.app.routing

import com.weekando.greeter.Greeter
import com.weekando.link.model.Link
import com.weekando.link.repository.LinkRepository
import io.ktor.application.*
import io.ktor.features.BadRequestException
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.LinkRoutes() {
    val greeter: Greeter by inject()
    val repository: LinkRepository by inject()

    route("/link") {
        get("/") {
            call.respond(HttpStatusCode.OK, mapOf("greeting" to greeter.hello("world")))
        }


        post("/") {
            val link: Link = call.receive(Link::class)
            println(link)

            repository.add(link)

            call.respond(HttpStatusCode.NoContent)
        }

        get("/{id}") {
            val id: String? = call.parameters["id"]

            if(null === id) {
                throw BadRequestException("Id is invalid")
            }

            val link: Link? = repository.get(id = id.toInt())

            if(null === link) {
                throw NotFoundException("Link $id does not exist")
            }

            call.respond(HttpStatusCode.OK, link)
        }
    }
}