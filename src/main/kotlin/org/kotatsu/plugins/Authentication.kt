package org.kotatsu.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.kotatsu.database
import org.kotatsu.model.user.UserEntity
import org.kotatsu.model.users
import org.ktorm.dsl.eq
import org.ktorm.entity.find

fun Application.configureAuthentication() {
	val config = this.environment.config
	val secret = config.property("jwt.secret").getString()
	val issuer = config.property("jwt.issuer").getString()
	val audience = config.property("jwt.audience").getString()
	install(Authentication) {
		jwt("auth-jwt") {
			verifier(
				JWT.require(Algorithm.HMAC256(secret))
					.withAudience(audience)
					.withIssuer(issuer)
					.build()
			)
			validate { credential ->
				if (credential.payload.getClaim("user_id")?.asInt() != null) {
					JWTPrincipal(credential.payload)
				} else {
					null
				}
			}
		}
	}
}

val ApplicationCall.currentUser: UserEntity?
	get() {
		val principal = principal<JWTPrincipal>() ?: return null
		val userId = principal.payload.getClaim("user_id").asInt()
		return database.users.find { it.id eq userId }
	}
