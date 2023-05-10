package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.user.*
import org.kotatsu.model.users
import org.kotatsu.util.md5
import org.kotatsu.util.withRetry
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.entity.find
import org.ktorm.entity.singleOrNull

suspend fun getOrCreateUser(request: AuthRequest): UserInfo? = withRetry {
	val passDigest = request.password.md5()
	val user = database.users.find { (it.email eq request.email) }
	when {
		user == null -> registerUser(request, passDigest)
		user.password == passDigest -> user.toUserInfo()
		else -> null
	}
}

private fun registerUser(request: AuthRequest, passwordDigest: String): UserInfo? {
	val userId = database.insertAndGenerateKey(UsersTable) {
		set(it.email, request.email)
		set(it.password, passwordDigest)
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