package org.kotatsu.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kotatsu.database
import org.kotatsu.model.favourite.FavouritesPackage
import org.kotatsu.model.history.HistoryPackage
import org.kotatsu.model.manga
import org.kotatsu.model.manga.toManga
import org.kotatsu.model.user.AuthRequest
import org.kotatsu.model.user.toUserInfo
import org.kotatsu.resources.*
import org.ktorm.database.TransactionIsolation
import org.ktorm.dsl.eq
import org.ktorm.entity.drop
import org.ktorm.entity.find
import org.ktorm.entity.map
import org.ktorm.entity.take
import java.util.*
import java.util.concurrent.TimeUnit

fun Application.configureRouting() {
	install(AutoHeadResponse)
	routing {
		authenticate("auth-jwt") {
			post<FavouritesPackage>("/resource/favourites") { request ->
				database.useTransaction(TransactionIsolation.READ_COMMITTED) {
					val user = call.currentUser
					if (user == null) {
						call.respond(HttpStatusCode.Unauthorized)
						return@post
					}
					val response = syncFavourites(user, request)
					user.setFavouritesSynchronized(System.currentTimeMillis())
					if (response.contentEquals(request)) {
						call.respond(HttpStatusCode.NoContent)
					} else {
						call.respond(response)
					}
				}
			}
			get("/resource/favourites") {
				val user = call.currentUser
				if (user == null) {
					call.respond(HttpStatusCode.Unauthorized)
					return@get
				}
				val response = syncFavourites(user, null)
				call.respond(response)
			}
			post<HistoryPackage>("/resource/history") { request ->
				database.useTransaction(TransactionIsolation.READ_COMMITTED) {
					val user = call.currentUser
					if (user == null) {
						call.respond(HttpStatusCode.Unauthorized)
						return@post
					}
					val response = syncHistory(user, request)
					user.setHistorySynchronized(System.currentTimeMillis())
					if (response.contentEquals(request)) {
						call.respond(HttpStatusCode.NoContent)
					} else {
						call.respond(response)
					}
				}
			}
			get("/resource/history") {
				database.useTransaction(TransactionIsolation.READ_COMMITTED) {
					val user = call.currentUser
					if (user == null) {
						call.respond(HttpStatusCode.Unauthorized)
						return@get
					}
					val response = syncHistory(user, null)
					call.respond(response)
				}
			}
			get("/me") {
				val user = call.currentUser
				if (user == null) {
					call.respond(HttpStatusCode.Unauthorized)
					return@get
				}
				call.respond(user.toUserInfo())
			}
		}
		post<AuthRequest>("/auth") { request ->
			val config = this@configureRouting.environment.config
			val secret = config.property("jwt.secret").getString()
			val issuer = config.property("jwt.issuer").getString()
			val audience = config.property("jwt.audience").getString()
			val allowNewRegister = config.property("kotatsu.allow_new_register").getString() == "true"
			val user = getOrCreateUser(request, allowNewRegister)
			if (user == null) {
				call.respondText(text = "Wrong password", status = HttpStatusCode.BadRequest)
				return@post
			}
			val lifetime = TimeUnit.DAYS.toMillis(30)
			val token = JWT.create()
				.withAudience(audience)
				.withIssuer(issuer)
				.withClaim("user_id", user.id)
				.withExpiresAt(Date(System.currentTimeMillis() + lifetime))
				.sign(Algorithm.HMAC256(secret))
			call.respond(hashMapOf("token" to token))
		}
		get("/") {
			call.respond(HttpStatusCode.OK, "Alive")
		}
		get("/manga/{id}") {
			val id = call.parameters["id"]?.toLongOrNull() ?: throw NotFoundException()
			val manga = database.manga.find { x -> x.id eq id }?.toManga() ?: throw NotFoundException()
			call.respond(manga)
		}
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
	}
}