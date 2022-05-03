package org.kotatsu.model.history

import org.kotatsu.model.manga.MangaTable
import org.kotatsu.model.user.UsersTable
import org.ktorm.schema.Table
import org.ktorm.schema.float
import org.ktorm.schema.int
import org.ktorm.schema.long

object HistoryTable : Table<HistoryEntity>("history") {

	val manga = long("manga_id").references(MangaTable) { it.manga }.primaryKey()

	val createdAt = long("created_at").bindTo { it.createdAt }
	val updatedAt = long("updated_at").bindTo { it.updatedAt }
	val chapterId = long("chapter_id").bindTo { it.chapterId }
	val page = int("page").bindTo { it.page }
	val scroll = float("scroll").bindTo { it.scroll }

	val userId = int("user_id").references(UsersTable) { it.user }.primaryKey()
}