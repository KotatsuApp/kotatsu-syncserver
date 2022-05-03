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
import org.kotatsu.model.user.RegisterRequest
import org.kotatsu.model.user.toUserInfo
import org.kotatsu.model.users
import org.kotatsu.resources.*
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import java.util.*
import java.util.concurrent.TimeUnit

fun Application.configureRouting() {

	routing {
		authenticate("auth-jwt") {
			post<FavouritesPackage>("/resource/favourites") { request ->
				database.useTransaction {
					val user = call.currentUser
					val response = syncFavourites(user, request)
					user.setFavouritesSynchronized(request.timestamp)
					call.respond(response)
				}
			}
			post<HistoryPackage>("/resource/history") { request ->
				database.useTransaction {
					val user = call.currentUser
					val response = syncHistory(user, request)
					user.setHistorySynchronized(request.timestamp)
					call.respond(response)
				}
			}
			get("/me") {
				call.respond(call.currentUser.toUserInfo())
			}
		}
		post<RegisterRequest>("/auth") { request ->
			val config = this@configureRouting.environment.config
			val secret = config.property("jwt.secret").getString()
			val issuer = config.property("jwt.issuer").getString()
			val audience = config.property("jwt.audience").getString()
			val user = database.users
				.find { (it.email eq request.email) and (it.password eq request.password) }
			if (user == null) {
				call.respond(HttpStatusCode.BadRequest)
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
		post<RegisterRequest>("/register") {
			val userInfo = registerUser(it)
			if (userInfo != null) {
				call.respond(HttpStatusCode.Created, userInfo)
			} else {
				call.respond(HttpStatusCode.BadRequest)
			}
		}
	}
}