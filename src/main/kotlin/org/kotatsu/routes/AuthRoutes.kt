package org.kotatsu.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.mustache.MustacheContent
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

    post<ForgotPasswordRequest>("/forgot-password") { request ->
        val config = application.environment.config
        val baseUrl = config.property("kotatsu.base_url").getString()

        val user = database.users.find { it.email eq request.email }
        if (user == null) {
            call.respond(HttpStatusCode.OK, "A password reset email was sent")
            return@post
        }

        val now = Instant.now().epochSecond
        val tokenExpiresAt = user.passwordResetTokenExpiresAt ?: 0L
        if (user.passwordResetTokenHash != null && tokenExpiresAt > now) {
            val secondsLeft = tokenExpiresAt - now
            val minutesLeft = (secondsLeft + 59) / 60

            call.respond(
                HttpStatusCode.BadRequest, "You can't reset your password for a $minutesLeft minutes"
            )
            return@post
        }

        val token = setPasswordResetToken(request, user)

        val resetPasswordLink = "${baseUrl}/reset-password?token=$token"

        val html = renderTemplateToString("mail/forgot-password.hbs", mapOf("reset_password_link" to resetPasswordLink))

        mailService.send(
            to = user.email, subject = "Password reset",
            textBody = "You can reset your password in $resetPasswordLink",
            htmlBody = html
        )

        call.respond(HttpStatusCode.OK, "A password reset email was sent")
    }

    get("/reset-password") {
        val token = call.request.queryParameters["token"]
        if (token.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Missing token")
            return@get
        }

        val deepLink = "kotatsu://reset-password?token=$token"

        call.respond(MustacheContent("pages/reset-password.hbs", mapOf("deep_link" to deepLink)))
    }

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
