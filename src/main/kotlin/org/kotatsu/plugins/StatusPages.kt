package org.kotatsu.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
	install(StatusPages) {
		exception<Throwable> { call, cause ->
			when (cause) {
				is IllegalArgumentException -> call.respondText(text = "400: $cause", status = HttpStatusCode.BadRequest)
				is NotFoundException -> call.respondText(text = "404: $cause", status = HttpStatusCode.NotFound)
				else -> {
					call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
					cause.printStackTrace()
				}
			}
		}
	}
}