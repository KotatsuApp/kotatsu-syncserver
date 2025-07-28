package org.kotatsu.routes

import io.ktor.server.plugins.NotFoundException
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.kotatsu.database
import org.kotatsu.model.manga
import org.kotatsu.model.manga.toManga
import org.ktorm.dsl.eq
import org.ktorm.entity.drop
import org.ktorm.entity.find
import org.ktorm.entity.map
import org.ktorm.entity.take
import kotlin.text.toIntOrNull
import kotlin.text.toLongOrNull

fun Route.mangaRoutes() {
	get("/manga") {
		val offset = requireNotNull(call.request.queryParameters["offset"]?.toIntOrNull()) {
			"Parameter \"offset\" is missing or invalid"
		}
		val limit = requireNotNull(call.request.queryParameters["limit"]?.toIntOrNull()) {
			"Parameter \"limit\" is missing or invalid"
		}
		val manga = database.manga.drop(offset).take(limit).map { it.toManga() }
		call.respond(manga)
	}
	get("/manga/{id}") {
		val id = call.parameters["id"]?.toLongOrNull() ?: throw NotFoundException()
		val manga = database.manga.find { x -> x.id eq id }?.toManga() ?: throw NotFoundException()
		call.respond(manga)
	}
}
