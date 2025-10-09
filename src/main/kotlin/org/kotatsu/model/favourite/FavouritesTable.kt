package org.kotatsu.model.favourite

import org.kotatsu.model.manga.MangaTable
import org.kotatsu.model.user.UsersTable
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.long

object FavouritesTable : Table<FavouriteEntity>("favourites") {
    val manga = long("manga_id").references(MangaTable) { it.manga }.primaryKey()
    val category = int("category_id").bindTo { it.categoryId }.primaryKey()
    val sortKey = int("sort_key").bindTo { it.sortKey }
    val pinned = boolean("pinned").bindTo { it.pinned }
    val createdAt = long("created_at").bindTo { it.createdAt }
    val deletedAt = long("deleted_at").bindTo { it.deletedAt }
    val userId = int("user_id").references(UsersTable) { it.user }.primaryKey()
}
