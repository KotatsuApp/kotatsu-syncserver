package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.manga
import org.kotatsu.model.manga.Manga
import org.kotatsu.model.manga.MangaEntity
import org.kotatsu.model.manga.MangaTagEntity
import org.kotatsu.model.manga.TagEntity
import org.kotatsu.model.mangaTags
import org.kotatsu.model.tags
import org.kotatsu.util.addOrUpdate
import org.kotatsu.util.toBoolean

fun upsertManga(manga: Manga): MangaEntity {
	val mangaEntity = MangaEntity {
		id = manga.id
		title = manga.title
		altTitle = manga.altTitle
		url = manga.url
		publicUrl = manga.publicUrl
		rating = manga.rating
		isNsfw = manga.isNsfw.toBoolean()
		coverUrl = manga.coverUrl
		state = manga.state
		author = manga.author
		source = manga.source
	}
	database.manga.addOrUpdate(mangaEntity)
	manga.tags.forEach { tag ->
		val tagEntity = TagEntity {
			id = tag.id
			title = tag.title
			key = tag.key
			source = tag.source
		}
		database.tags.addOrUpdate(tagEntity)
		val mangaTagEntity = MangaTagEntity {
			this@MangaTagEntity.manga = mangaEntity
			this@MangaTagEntity.tag = tagEntity
		}
		database.mangaTags.addOrUpdate(mangaTagEntity)
	}
	return mangaEntity
}