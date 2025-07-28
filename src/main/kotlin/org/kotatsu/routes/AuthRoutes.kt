package org.kotatsu.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import org.kotatsu.model.user.AuthRequest
import org.kotatsu.resources.getOrCreateUser
import java.util.Date
import java.util.concurrent.TimeUnit

fun Route.authRoutes(){
	post<AuthRequest>("/auth") { request ->
		val config = application.environment.config
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
}
