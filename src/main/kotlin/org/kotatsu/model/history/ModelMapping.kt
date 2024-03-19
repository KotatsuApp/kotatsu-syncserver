package org.kotatsu.model.history

import org.kotatsu.model.manga.MangaEntity
import org.kotatsu.model.manga.toManga
import org.kotatsu.model.user.UserEntity

fun HistoryEntity.toHistory() = History(
	mangaId = manga.id,
	manga = manga.toManga(),
	createdAt = createdAt,
	updatedAt = updatedAt,
	chapterId = chapterId,
	page = page,
	scroll = scroll,
	percent = percent,
	chapters = chapters,
	deletedAt = deletedAt,
)

fun History.toEntity(
	mangaEntity: MangaEntity,
	userEntity: UserEntity,
) = HistoryEntity {
	manga = mangaEntity
	createdAt = this@toEntity.createdAt
	updatedAt = this@toEntity.updatedAt
	chapterId = this@toEntity.chapterId
	page = this@toEntity.page
	scroll = this@toEntity.scroll
	percent = this@toEntity.percent
	chapters = this@toEntity.chapters
	deletedAt = this@toEntity.deletedAt
	user = userEntity
}