package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.categories
import org.kotatsu.model.favourite.*
import org.kotatsu.model.favourites
import org.kotatsu.model.user.UserEntity
import org.kotatsu.util.toBoolean
import org.kotatsu.util.truncate
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.map
import org.ktorm.support.mysql.bulkInsertOrUpdate
import org.ktorm.support.mysql.insertOrUpdate

fun syncFavourites(
	user: UserEntity,
	request: FavouritesPackage?,
): FavouritesPackage {
	if (request != null) {
		if (request.favouriteCategories.isNotEmpty()) {
			database.bulkInsertOrUpdate(CategoriesTable) {
				for (category in request.favouriteCategories) {
					item {
						set(it.id, category.id)
						set(it.createdAt, category.createdAt)
						set(it.sortKey, category.sortKey)
						set(it.title, category.title.truncate(120))
						set(it.order, category.order)
						set(it.track, category.track.toBoolean())
						set(it.showInLib, category.showInLib.toBoolean())
						set(it.deletedAt, category.deletedAt)
						set(it.userId, user.id)
					}
				}
			}
		}
		for (favourite in request.favourites) {
			upsertManga(favourite.manga)
			database.insertOrUpdate(FavouritesTable) {
				set(it.manga, favourite.mangaId)
				set(it.category, favourite.categoryId)
				set(it.sortKey, favourite.sortKey)
				set(it.createdAt, favourite.createdAt)
				set(it.deletedAt, favourite.deletedAt)
				set(it.userId, user.id)
			}
		}
	}
	return FavouritesPackage(
		favouriteCategories = database.categories
			.filter { it.userId eq user.id }
			.map { it.toCategory() },
		favourites = database.favourites
			.filter { it.userId eq user.id }
			.map { it.toFavourite() },
		timestamp = user.favouritesSyncTimestamp ?: 0L
	)
}