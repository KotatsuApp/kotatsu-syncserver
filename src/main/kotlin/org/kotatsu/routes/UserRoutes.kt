package org.kotatsu.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.auth.*
import org.kotatsu.model.user.toUserInfo
import org.kotatsu.plugins.currentUser

fun Route.userRoutes() {
    authenticate("auth-jwt") {
        get("/me") {
            val user = call.currentUser
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }
            call.respond(user.toUserInfo())
        }
    }
}
