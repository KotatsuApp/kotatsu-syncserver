package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.categories
import org.kotatsu.model.favourite.FavouritesPackage
import org.kotatsu.model.favourite.toCategory
import org.kotatsu.model.favourite.toEntity
import org.kotatsu.model.favourite.toFavourite
import org.kotatsu.model.favourites
import org.kotatsu.model.user.UserEntity
import org.kotatsu.util.addOrUpdate
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.map

fun syncFavourites(
	user: UserEntity,
	request: FavouritesPackage,
): FavouritesPackage = database.useTransaction {
	for (category in request.favouriteCategories) {
		val entity = category.toEntity(user)
		database.categories.addOrUpdate(entity)
	}
	for (favourite in request.favourites) {
		val mangaEntity = upsertManga(favourite.manga)
		val entity = favourite.toEntity(mangaEntity, user)
		database.favourites.addOrUpdate(entity)
	}
	FavouritesPackage(
		favouriteCategories = database.categories.filter { it.userId eq user.id }
			.map { it.toCategory() },
		favourites = database.favourites
			.filter { it.userId eq user.id }
			.map { it.toFavourite() },
		timestamp = System.currentTimeMillis(),
	)
}