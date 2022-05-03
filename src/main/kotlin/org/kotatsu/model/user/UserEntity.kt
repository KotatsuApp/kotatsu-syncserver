package org.kotatsu.model.user

import org.ktorm.entity.Entity

interface UserEntity : Entity<UserEntity> {

	var id: Int
	var email: String
	var password: String
	var nickname: String?
	var favouritesSyncTimestamp: Long?
	var historySyncTimestamp: Long?

	companion object : Entity.Factory<UserEntity>()
}