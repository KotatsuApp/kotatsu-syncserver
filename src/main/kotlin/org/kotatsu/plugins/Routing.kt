package org.kotatsu.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kotatsu.database
import org.kotatsu.model.favourite.FavouritesPackage
import org.kotatsu.model.history.HistoryPackage
import org.kotatsu.model.user.AuthRequest
import org.kotatsu.model.user.toUserInfo
import org.kotatsu.resources.*
import org.ktorm.database.TransactionIsolation
import java.util.*
import java.util.concurrent.TimeUnit

fun Application.configureRouting() {
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
			val user = getOrCreateUser(request)
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
	}
}