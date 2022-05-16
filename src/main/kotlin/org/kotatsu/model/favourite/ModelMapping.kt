package org.kotatsu.model.favourite

import org.kotatsu.model.manga.MangaEntity
import org.kotatsu.model.manga.toManga
import org.kotatsu.model.user.UserEntity
import org.kotatsu.util.toBoolean
import org.kotatsu.util.toInt

fun CategoryEntity.toCategory() = Category(
	id = id,
	createdAt = createdAt,
	sortKey = sortKey,
	title = title,
	order = order,
	track = track.toInt(),
	deletedAt = deletedAt,
)

fun Category.toEntity(userEntity: UserEntity) = CategoryEntity {
	id = this@toEntity.id
	createdAt = this@toEntity.createdAt
	sortKey = this@toEntity.sortKey
	title = this@toEntity.title
	order = this@toEntity.order
	track = this@toEntity.track.toBoolean()
	deletedAt = this@toEntity.deletedAt
	user = userEntity
}

fun FavouriteEntity.toFavourite() = Favourite(
	mangaId = manga.id,
	manga = manga.toManga(),
	categoryId = categoryId,
	createdAt = createdAt,
	deletedAt = deletedAt,
)

fun Favourite.toEntity(
	mangaEntity: MangaEntity,
	userEntity: UserEntity,
) = FavouriteEntity {
	manga = mangaEntity
	categoryId = this@toEntity.categoryId
	createdAt = this@toEntity.createdAt
	deletedAt = this@toEntity.deletedAt
	user = userEntity
}