package org.kotatsu.resources

import org.kotatsu.model.manga
import org.kotatsu.model.manga.*
import org.kotatsu.model.tags
import org.kotatsu.util.toBoolean
import org.kotatsu.util.truncated
import org.kotatsu.util.withRetry
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.find
import org.ktorm.support.mysql.insertOrUpdate
import java.sql.SQLIntegrityConstraintViolationException

suspend fun Database.upsertManga(manga: Manga) {
	val existedManga = this.manga.find { x -> x.id eq manga.id }?.toManga()
	if (existedManga == manga) {
		return
	}
	withRetry {
		insertOrUpdate(MangaTable) {
			set(it.id, manga.id)
			set(it.title, manga.title.truncated(84))
			set(it.altTitle, manga.altTitle?.truncated(84))
			set(it.url, manga.url.truncated(255))
			set(it.publicUrl, manga.publicUrl.truncated(255))
			set(it.rating, manga.rating)
			set(it.isNsfw, manga.isNsfw.toBoolean())
			set(it.coverUrl, manga.coverUrl.truncated(255))
			set(it.state, manga.state)
			set(it.author, manga.author?.truncated(32))
			set(it.mangaSource, manga.source.truncated(32))
			onDuplicateKey {
				set(it.title, manga.title.truncated(84))
				set(it.altTitle, manga.altTitle?.truncated(84))
				set(it.url, manga.url.truncated(255))
				set(it.publicUrl, manga.publicUrl.truncated(255))
				set(it.rating, manga.rating)
				set(it.isNsfw, manga.isNsfw.toBoolean())
				set(it.coverUrl, manga.coverUrl.truncated(255))
				set(it.state, manga.state)
				set(it.author, manga.author?.truncated(32))
				set(it.mangaSource, manga.source.truncated(32))
			}
		}
		for (tag in manga.tags) {
			val existedTag = tags.find { x -> x.id eq tag.id }?.toTag()
			if (existedTag != tag) {
				insertOrUpdate(TagsTable) {
					set(it.id, tag.id)
					set(it.title, tag.title.truncated(64))
					set(it.key, tag.key.truncated(120))
					set(it.source, tag.source.truncated(32))
					onDuplicateKey {
						set(it.title, tag.title.truncated(64))
						set(it.key, tag.key.truncated(120))
						set(it.source, tag.source.truncated(32))
					}
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
}