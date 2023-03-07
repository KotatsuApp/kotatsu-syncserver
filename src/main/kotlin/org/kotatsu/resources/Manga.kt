package org.kotatsu.resources

import org.kotatsu.model.manga.Manga
import org.kotatsu.model.manga.MangaTable
import org.kotatsu.model.manga.MangaTagsTable
import org.kotatsu.model.manga.TagsTable
import org.kotatsu.util.toBoolean
import org.kotatsu.util.truncate
import org.kotatsu.util.withRetry
import org.ktorm.database.Database
import org.ktorm.dsl.insert
import org.ktorm.support.mysql.insertOrUpdate
import java.sql.SQLIntegrityConstraintViolationException

suspend fun Database.upsertManga(manga: Manga) = withRetry {
	insertOrUpdate(MangaTable) {
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
		onDuplicateKey {
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
	}
	for (tag in manga.tags) {
		insertOrUpdate(TagsTable) {
			set(it.id, tag.id)
			set(it.title, tag.title.truncate(64))
			set(it.key, tag.key.truncate(120))
			set(it.source, tag.source.truncate(32))
			onDuplicateKey {
				set(it.title, tag.title.truncate(64))
				set(it.key, tag.key.truncate(120))
				set(it.source, tag.source.truncate(32))
			}
		}
		try {
			insert(MangaTagsTable) {
				set(it.manga, manga.id)
				set(it.tag, tag.id)
			}
		} catch (e: SQLIntegrityConstraintViolationException) {
			// skip
		}
	}
}