package org.kotatsu.model.manga

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object TagsTable : Table<TagEntity>("tags") {
    val id = long("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    val key = varchar("key").bindTo { it.key }
    val source = varchar("source").bindTo { it.source }
}
