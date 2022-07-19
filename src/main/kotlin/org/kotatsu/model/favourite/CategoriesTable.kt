package org.kotatsu.model.favourite

import org.kotatsu.model.user.UsersTable
import org.ktorm.schema.*

object CategoriesTable : Table<CategoryEntity>("categories") {

	val id = int("id").primaryKey().bindTo { it.id }
	val createdAt = long("created_at").bindTo { it.createdAt }
	val sortKey = int("sort_key").bindTo { it.sortKey }
	val track = boolean("track").bindTo { it.track }
	val title = varchar("title").bindTo { it.title }
	val order = varchar("order").bindTo { it.order }
	val deletedAt = long("deleted_at").bindTo { it.deletedAt }
	val showInLib = boolean("show_in_lib").bindTo { it.showInLib }

	val userId = int("user_id").primaryKey().references(UsersTable) { it.user }
}