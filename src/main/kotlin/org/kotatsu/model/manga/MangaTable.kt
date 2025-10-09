package org.kotatsu.model.manga

import org.ktorm.schema.*

object MangaTable : Table<MangaEntity>("manga") {
    val id = long("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    val altTitle = varchar("alt_title").bindTo { it.altTitle }
    val url = varchar("url").bindTo { it.url }
    val publicUrl = varchar("public_url").bindTo { it.publicUrl }
    val rating = float("rating").bindTo { it.rating }
    val contentRating = enum<ContentRating>("content_rating").bindTo { it.contentRating }
    val coverUrl = varchar("cover_url").bindTo { it.coverUrl }
    val largeCoverUrl = varchar("large_cover_url").bindTo { it.largeCoverUrl }
    val state = enum<MangaState>("state").bindTo { it.state }
    val author = text("author").bindTo { it.author }
    val mangaSource = text("source").bindTo { it.source }
}
