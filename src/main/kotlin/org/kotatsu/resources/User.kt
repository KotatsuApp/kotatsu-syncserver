package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.user.AuthRequest
import org.kotatsu.model.user.UserEntity
import org.kotatsu.model.user.UserInfo
import org.kotatsu.model.user.toUserInfo
import org.kotatsu.model.users
import org.kotatsu.util.md5
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find

fun getOrCreateUser(request: AuthRequest): UserInfo? {
	val passDigest = request.password.md5()
	val user = database.users.find { (it.email eq request.email) }
	return when {
		user == null -> registerUser(request, passDigest)
		user.password == passDigest -> user.toUserInfo()
		else -> null
	}
}

private fun registerUser(request: AuthRequest, passwordDigest: String): UserInfo? {
	val entity = UserEntity {
		email = request.email
		password = passwordDigest
		nickname = null
		favouritesSyncTimestamp = null
		historySyncTimestamp = null
	}
	return if (database.users.add(entity) > 0 && entity.id > 0) {
		entity.toUserInfo()
	} else {
		null
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