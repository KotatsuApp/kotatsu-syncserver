package org.kotatsu.model.favourite

import org.kotatsu.model.manga.MangaEntity
import org.kotatsu.model.user.UserEntity
import org.ktorm.entity.Entity

interface FavouriteEntity : Entity<FavouriteEntity> {

	var manga: MangaEntity
	var categoryId: Int
	var sortKey: Int
	var createdAt: Long
	var deletedAt: Long

	var user: UserEntity

	companion object : Entity.Factory<FavouriteEntity>()
}