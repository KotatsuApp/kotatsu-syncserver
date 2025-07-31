package org.kotatsu.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kotatsu.database
import org.kotatsu.model.history.HistoryPackage
import org.kotatsu.plugins.currentUser
import org.kotatsu.resources.setHistorySynchronized
import org.kotatsu.resources.syncHistory
import org.ktorm.database.TransactionIsolation

fun Route.historyRoutes() {
	authenticate("auth-jwt") {
		get("/resource/history") {
			val user = call.currentUser
			if (user == null) {
				call.respond(HttpStatusCode.Unauthorized)
				return@get
			}

			val response = withContext(Dispatchers.IO) {
				database.useTransaction(TransactionIsolation.READ_COMMITTED) {
					syncHistory(user, null)
				}
			}

			call.respond(response)
		}
		post<HistoryPackage>("/resource/history") { request ->
			val user = call.currentUser
			if (user == null) {
				call.respond(HttpStatusCode.Unauthorized)
				return@post
			}

			val response = withContext(Dispatchers.IO) {
				database.useTransaction(TransactionIsolation.READ_COMMITTED) {
					val result = syncHistory(user, request)
					user.setHistorySynchronized(System.currentTimeMillis())
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
