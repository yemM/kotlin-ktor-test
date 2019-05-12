package com.weekando

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.date.*
import io.ktor.routing.*
import com.weekando.app.container.GreeterModule
import com.weekando.app.container.ORMModule
import com.weekando.app.orm.Link
import com.weekando.app.orm.pool.ConnectionPool
import com.weekando.app.routing.LinkRoutes
import io.ktor.jackson.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    install(Koin) {
        fileProperties("/koin.properties")
        modules(
            GreeterModule,
            ORMModule
        )
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(CachingHeaders) {
        options { outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Text.CSS -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60), expires = null as? GMTDate?)
                else -> null
            }
        }
    }

    install(DefaultHeaders) {
        val env = environment.config.property("ktor.env").getString()
        header("X-Engine", "Ktor ($env)") // will send this header with each response
    }

    install(ContentNegotiation) {
        jackson {
        }
    }

    if("true" === environment.config.property("ktor.testing").getString()) {
        val connectionPool : ConnectionPool = get()
        Database.connect(connectionPool.create())

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Link)
        }
    }

    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, mapOf("HELLO" to "WORLD"))
        }

        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }

        }

        LinkRoutes()
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

