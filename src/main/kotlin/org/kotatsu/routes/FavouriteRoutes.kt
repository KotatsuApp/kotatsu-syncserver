package org.kotatsu.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondText
import io.ktor.util.toMap
import org.kotatsu.database
import org.kotatsu.model.favourite.FavouritesPackage
import org.kotatsu.plugins.currentUser
import org.kotatsu.resources.setFavouritesSynchronized
import org.kotatsu.resources.syncFavourites
import org.ktorm.database.TransactionIsolation

fun Route.favouriteRoutes() {
	authenticate("auth-jwt") {
		get("/resource/favourites") {
			val user = call.currentUser
			if (user == null) {
				call.respond(HttpStatusCode.Unauthorized)
				return@get
			}
			val response = syncFavourites(user, null)
			call.respond(response)
		}
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
	}
}
