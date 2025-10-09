package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.categories
import org.kotatsu.model.category.*
import org.kotatsu.model.favourite.*
import org.kotatsu.model.favourites
import org.kotatsu.model.user.UserEntity
import org.kotatsu.util.truncated
import org.kotatsu.util.withRetry
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.map
import org.ktorm.support.mysql.insertOrUpdate

suspend fun syncFavourites(
    user: UserEntity,
    request: FavouritesPackage?,
): FavouritesPackage {
    if (request != null) {
        for (category in request.favouriteCategories) {
            database.upsertCategory(category, user.id)
        }
        for (favourite in request.favourites) {
            database.upsertManga(favourite.manga)
            database.upsertFavourite(favourite, user.id)
        }
    }
    return FavouritesPackage(
        favouriteCategories = database.categories
            .filter { it.userId eq user.id }
            .map { it.toCategory() },
        favourites = database.favourites
            .filter { it.userId eq user.id }
            .map { it.toFavourite() },
        timestamp = user.favouritesSyncTimestamp ?: 0L,
    )
}

private suspend fun Database.upsertCategory(category: Category, userId: Int) {
    val existedCategory = categories.find { x -> (x.id eq category.id) and (x.userId eq userId) }?.toCategory()
    if (existedCategory == category) {
        return
    }
    withRetry {
        insertOrUpdate(CategoriesTable) {
            set(it.id, category.id)
            set(it.createdAt, category.createdAt)
            set(it.sortKey, category.sortKey)
            set(it.title, category.title.truncated(120))
            set(it.order, category.order)
            set(it.track, category.track)
            set(it.showInLib, category.showInLib)
            set(it.deletedAt, category.deletedAt)
            set(it.userId, userId)
            onDuplicateKey {
                set(it.createdAt, category.createdAt)
                set(it.sortKey, category.sortKey)
                set(it.title, category.title.truncated(120))
                set(it.order, category.order)
                set(it.track, category.track)
                set(it.showInLib, category.showInLib)
                set(it.deletedAt, category.deletedAt)
            }
        }
    }
}

private suspend fun Database.upsertFavourite(favourite: Favourite, userId: Int) {
    val existed = favourites.find { x ->
        (x.manga eq favourite.mangaId) and (x.userId eq userId) and (x.category eq favourite.categoryId)
    }?.toFavourite()
    if (existed == favourite) {
        return
    }
    withRetry {
        insertOrUpdate(FavouritesTable) {
            set(it.manga, favourite.mangaId)
            set(it.category, favourite.categoryId)
            set(it.sortKey, favourite.sortKey)
            set(it.pinned, favourite.pinned)
            set(it.createdAt, favourite.createdAt)
            set(it.deletedAt, favourite.deletedAt)
            set(it.userId, userId)
            onDuplicateKey {
                set(it.sortKey, favourite.sortKey)
                set(it.pinned, favourite.pinned)
                set(it.createdAt, favourite.createdAt)
                set(it.deletedAt, favourite.deletedAt)
            }
        }
    }
}
