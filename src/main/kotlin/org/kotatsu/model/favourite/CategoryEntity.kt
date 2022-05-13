package org.kotatsu.model.favourite

import org.kotatsu.model.user.UserEntity
import org.ktorm.entity.Entity

interface CategoryEntity : Entity<CategoryEntity> {

	var id: Int
	var createdAt: Long
	var sortKey: Int
	var track: Boolean
	var title: String
	var order: String

	var user: UserEntity

	companion object : Entity.Factory<CategoryEntity>()
}