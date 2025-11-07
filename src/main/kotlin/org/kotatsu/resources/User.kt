package org.kotatsu.resources

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kotatsu.database
import org.kotatsu.model.user.*
import org.kotatsu.model.users
import org.kotatsu.util.withRetry
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.entity.find
import org.ktorm.entity.singleOrNull
import org.kotatsu.util.PasswordHasher
import org.kotatsu.util.generateSecureToken
import org.kotatsu.util.md5
import org.kotatsu.util.sha256Hex
import org.ktorm.dsl.and
import org.ktorm.dsl.greaterEq
import org.ktorm.dsl.update
import java.time.Instant

suspend fun getOrCreateUser(request: AuthRequest, allowNewRegister: Boolean): UserInfo? = withRetry {
    require(request.password.length in 2..24) {
        "Password should be from 2 to 24 characters long"
    }
    require(request.email.length in 5..320 && '@' in request.email) {
        "Invalid email address"
    }

    val user = database.users.find { (it.email eq request.email) }

    when {
        user == null && allowNewRegister -> {
            val newPasswordHash = withContext(Dispatchers.Default) {
                PasswordHasher.hash(request.password)
            }

            registerUser(request, newPasswordHash)
        }

        user == null -> null
        else -> {
            val storedHash = user.passwordHash

            when {
                PasswordHasher.isArgon2Hash(storedHash) -> {
                    val verified = withContext(Dispatchers.Default) {
                        PasswordHasher.verify(request.password, storedHash)
                    }
                    if (verified) user.toUserInfo() else null
                }

                else -> { // MD5 legacy
                    if (storedHash == request.password.md5()) {
                        // Upgrade to argon2id
                        user.passwordHash = PasswordHasher.hash(request.password)
                        user.flushChanges()
                        user.toUserInfo()
                    } else null
                }
            }
        }
    }
}

private fun registerUser(request: AuthRequest, passwordDigest: String): UserInfo? {
    val userId = database.insertAndGenerateKey(UsersTable) {
        set(it.email, request.email)
        set(it.passwordHash, passwordDigest)
        set(it.nickname, null)
        set(it.favouritesSyncTimestamp, null)
        set(it.historySyncTimestamp, null)
    } as Int
    return database.users
        .singleOrNull { (it.id eq userId) }
        ?.toUserInfo()
}

fun setPasswordResetToken(request: ForgotPasswordRequest, user: UserEntity): String {
    val token = generateSecureToken()
    val tokenHash = sha256Hex(token)
    val now = Instant.now().epochSecond
    val expiresAt = now + 900 // 15 minutes

    database.update(UsersTable) {
        set(it.passwordResetTokenHash, tokenHash)
        set(it.passwordResetTokenExpiresAt, expiresAt)
        where { it.id eq user.id }
    }

    return token
}

fun UserEntity.resetPassword(newPasswordHash: String) {
    passwordHash = newPasswordHash
    passwordResetTokenHash = null
    passwordResetTokenExpiresAt = null
    flushChanges()
}

fun findUserByValidPasswordResetToken(token: String): UserEntity? {
    val now = Instant.now().epochSecond
    val tokenHash = sha256Hex(token)

    return database.users.find {
        (it.passwordResetTokenHash eq tokenHash) and
            (it.passwordResetTokenExpiresAt greaterEq now)
    }
}

fun UserEntity.setFavouritesSynchronized(timestamp: Long) {
    favouritesSyncTimestamp = timestamp
    flushChanges()
}

fun UserEntity.setHistorySynchronized(timestamp: Long) {
    historySyncTimestamp = timestamp
    flushChanges()
}
