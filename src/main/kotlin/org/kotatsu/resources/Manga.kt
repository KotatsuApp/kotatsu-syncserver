package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.manga.Manga
import org.kotatsu.model.manga.MangaTable
import org.kotatsu.model.manga.MangaTagsTable
import org.kotatsu.model.manga.TagsTable
import org.kotatsu.util.toBoolean
import org.kotatsu.util.truncate
import org.ktorm.support.mysql.bulkInsertOrUpdate
import org.ktorm.support.mysql.insertOrUpdate

fun upsertManga(manga: Manga) {
	database.insertOrUpdate(MangaTable) {
		set(it.id, manga.id)
		set(it.title, manga.title.truncate(84))
		set(it.altTitle, manga.altTitle?.truncate(84))
		set(it.url, manga.url.truncate(255))
		set(it.publicUrl, manga.publicUrl.truncate(255))
		set(it.rating, manga.rating)
		set(it.isNsfw, manga.isNsfw.toBoolean())
		set(it.coverUrl, manga.coverUrl.truncate(255))
		set(it.state, manga.state)
		set(it.author, manga.author?.truncate(32))
		set(it.mangaSource, manga.source.truncate(32))
	}
	if (manga.tags.isNotEmpty()) {
		database.bulkInsertOrUpdate(TagsTable) {
			for (tag in manga.tags) {
				item {
					set(it.id, tag.id)
					set(it.title, tag.title.truncate(64))
					set(it.key, tag.key.truncate(120))
					set(it.source, tag.source.truncate(32))
				}
			}
		}
		database.bulkInsertOrUpdate(MangaTagsTable) {
			for (tag in manga.tags) {
				item {
					set(it.manga, manga.id)
					set(it.tag, tag.id)
				}
			}
		}
	}
}