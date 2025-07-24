package org.kotatsu.model.favourite

import org.kotatsu.model.manga.MangaEntity
import org.kotatsu.model.manga.toManga
import org.kotatsu.model.user.UserEntity
import org.kotatsu.util.truncated

fun CategoryEntity.toCategory() = Category(
	id = id,
	createdAt = createdAt,
	sortKey = sortKey,
	title = title,
	order = order,
	track = track,
	showInLib = showInLib,
	deletedAt = deletedAt,
)

fun Category.toEntity(userEntity: UserEntity) = CategoryEntity {
	id = this@toEntity.id
	createdAt = this@toEntity.createdAt
	sortKey = this@toEntity.sortKey
	title = this@toEntity.title.truncated(120)
	order = this@toEntity.order
	track = this@toEntity.track
	showInLib = this@toEntity.showInLib
	deletedAt = this@toEntity.deletedAt
	user = userEntity
}

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