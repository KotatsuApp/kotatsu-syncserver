package org.kotatsu.model.manga

import org.ktorm.schema.Table
import org.ktorm.schema.long

object MangaTagsTable : Table<MangaTagEntity>("manga_tags") {
	val manga = long("manga_id").primaryKey().references(MangaTable) { it.manga }
	val tag = long("tag_id").primaryKey().references(TagsTable) { it.tag }
}
