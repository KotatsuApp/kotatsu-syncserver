package org.kotatsu.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import org.kotatsu.database
import org.kotatsu.mailService
import org.kotatsu.model.user.AuthRequest
import org.kotatsu.model.user.ForgotPasswordRequest
import org.kotatsu.model.user.ResetPasswordRequest
import org.kotatsu.model.users
import org.kotatsu.resources.*
import org.kotatsu.util.*
import org.ktorm.dsl.*
import org.ktorm.entity.find
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.collections.mapOf

fun Route.authRoutes() {
    rateLimit(RateLimitName("auth")) {
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

    rateLimit(RateLimitName("forgotPassword")) {
        post<ForgotPasswordRequest>("/forgot-password") { request ->
            val baseUrl = application.environment.config.property("kotatsu.base_url").getString()

            val user = database.users.find { it.email eq request.email }
            val now = Instant.now().epochSecond

            val canSend = user?.let {
                val expiresAt = it.passwordResetTokenExpiresAt ?: 0L
                val activeToken = it.passwordResetTokenHash != null && expiresAt > now
                !activeToken
            } ?: false

            if (canSend) {
                val token = setPasswordResetToken(request, user)
                val link = "$baseUrl/deeplink/reset-password?token=$token"
                val html = renderTemplateToString(
                    "mail/forgot-password.hbs",
                    mapOf("reset_password_link" to link)
                )

                mailService.send(
                    to = user.email,
                    subject = "Password reset",
                    textBody = "You can reset your password at $link",
                    htmlBody = html
                )
            }

            call.respond(HttpStatusCode.OK, "A password reset email was sent")
        }
    }

    rateLimit(RateLimitName("resetPassword")) {
        post<ResetPasswordRequest>("/reset-password") { request ->
            val token = request.resetToken

            val user = findUserByValidPasswordResetToken(token)
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid or expired token")
                return@post
            }

            if (request.password.length !in 2..24) {
                call.respond(HttpStatusCode.BadRequest, "Password should be from 2 to 24 characters long")
                return@post
            }

            val newPasswordHash = PasswordHasher.hash(request.password)

            user.resetPassword(newPasswordHash)

            call.respond(HttpStatusCode.OK, "Password has been reset successfully")
        }
    }
}
