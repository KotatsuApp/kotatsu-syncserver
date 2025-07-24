package org.kotatsu.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
			val user = call.currentUser
			if (user == null) {
				call.respond(HttpStatusCode.Unauthorized)
				return@post
			}

			val response = withContext(Dispatchers.IO) {
				database.useTransaction(TransactionIsolation.READ_COMMITTED) {
					val result = syncFavourites(user, request)
					user.setFavouritesSynchronized(System.currentTimeMillis())
					result
				}
			}

			if (response.contentEquals(request)) {
				call.respond(HttpStatusCode.NoContent)
			} else {
				call.respond(response)
			}
		}
	}
}
