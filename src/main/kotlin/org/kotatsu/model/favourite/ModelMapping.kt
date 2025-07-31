package org.kotatsu.model.favourite

import org.kotatsu.model.manga.MangaEntity
import org.kotatsu.model.manga.toManga
import org.kotatsu.model.user.UserEntity

fun FavouriteEntity.toFavourite() = Favourite(
	mangaId = manga.id,
	manga = manga.toManga(),
	categoryId = categoryId,
	sortKey = sortKey,
	pinned = pinned,
	createdAt = createdAt,
	deletedAt = deletedAt,
)

fun Favourite.toEntity(
	mangaEntity: MangaEntity,
	userEntity: UserEntity,
) = FavouriteEntity {
	manga = mangaEntity
	categoryId = this@toEntity.categoryId
	sortKey = this@toEntity.sortKey
	createdAt = this@toEntity.createdAt
	deletedAt = this@toEntity.deletedAt
	user = userEntity
}
