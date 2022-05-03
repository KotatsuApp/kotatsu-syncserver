package org.kotatsu.model.favourite

import org.kotatsu.model.manga.MangaEntity
import org.kotatsu.model.manga.toManga
import org.kotatsu.model.user.UserEntity

fun CategoryEntity.toCategory() = Category(
	id = id,
	createdAt = createdAt,
	sortKey = sortKey,
	title = title,
	order = order,
)

fun Category.toEntity(userEntity: UserEntity) = CategoryEntity {
	id = this@toEntity.id
	createdAt = this@toEntity.createdAt
	sortKey = this@toEntity.sortKey
	title = this@toEntity.title
	order = this@toEntity.order
	user = userEntity
}

fun FavouriteEntity.toFavourite() = Favourite(
	mangaId = manga.id,
	manga = manga.toManga(),
	categoryId = categoryId,
	createdAt = createdAt,
)

fun Favourite.toEntity(
	mangaEntity: MangaEntity,
	userEntity: UserEntity,
) = FavouriteEntity {
	manga = mangaEntity
	categoryId = categoryId
	createdAt = this@toEntity.createdAt
	user = userEntity
}