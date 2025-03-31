package org.kotatsu.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import org.kotatsu.database
import org.kotatsu.model.history.HistoryPackage
import org.kotatsu.plugins.currentUser
import org.kotatsu.resources.setHistorySynchronized
import org.kotatsu.resources.syncHistory
import org.ktorm.database.TransactionIsolation

fun Route.historyRoutes() {
	authenticate("auth-jwt") {
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
	}
}