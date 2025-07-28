package org.kotatsu.model.history

import org.kotatsu.model.manga.MangaEntity
import org.kotatsu.model.user.UserEntity
import org.ktorm.entity.Entity

interface HistoryEntity : Entity<HistoryEntity> {
	var manga: MangaEntity

	var createdAt: Long
	var updatedAt: Long
	var chapterId: Long
	var page: Int
	var scroll: Float
	var percent: Float
	var chapters: Int
	var deletedAt: Long

	var user: UserEntity

	companion object : Entity.Factory<HistoryEntity>()
}
