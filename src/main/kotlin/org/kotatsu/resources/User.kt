package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.user.RegisterRequest
import org.kotatsu.model.user.UserEntity
import org.kotatsu.model.user.UserInfo
import org.kotatsu.model.user.toUserInfo
import org.kotatsu.model.users
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find

fun getUserInfo(id: Int): UserInfo? {
	return database.users.find { it.id eq id }?.toUserInfo()
}

fun registerUser(request: RegisterRequest): UserInfo? {
	val entity = UserEntity {
		email = request.email
		password = request.password
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