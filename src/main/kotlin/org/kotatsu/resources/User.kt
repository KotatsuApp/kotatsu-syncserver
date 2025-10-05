package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.user.*
import org.kotatsu.model.users
import org.kotatsu.util.withRetry
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.entity.find
import org.ktorm.entity.singleOrNull
import org.kotatsu.util.PasswordHasher
import org.kotatsu.util.md5

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
            val bcryptHash = PasswordHasher.hash(request.password)
            registerUser(request, bcryptHash)
        }

        user == null -> null
        else -> {
            val storedHash = user.passwordHash

            when {
                PasswordHasher.isArgon2Hash(storedHash) -> {
                    if (PasswordHasher.verify(request.password, storedHash)) {
                        user.toUserInfo()
                    } else null
                }

                else -> { // MD5 legacy
                    if (storedHash == request.password.md5()) {
                        // Upgrade to bcrypt
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

fun UserEntity.setFavouritesSynchronized(timestamp: Long) {
    favouritesSyncTimestamp = timestamp
    flushChanges()
}

fun UserEntity.setHistorySynchronized(timestamp: Long) {
    historySyncTimestamp = timestamp
    flushChanges()
}
