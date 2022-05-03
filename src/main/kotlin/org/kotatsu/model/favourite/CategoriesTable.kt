package org.kotatsu.model.favourite

import org.kotatsu.model.user.UsersTable
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object CategoriesTable : Table<CategoryEntity>("categories") {

	val id = int("id").primaryKey().bindTo { it.id }
	val createdAt = long("created_at").bindTo { it.createdAt }
	val sortKey = int("sort_key").bindTo { it.sortKey }
	val title = varchar("title").bindTo { it.title }
	val order = varchar("order").bindTo { it.order }

	val userId = int("user_id").primaryKey().references(UsersTable) { it.user }
}