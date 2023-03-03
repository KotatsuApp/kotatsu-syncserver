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
import org.kotatsu.util.truncate

fun upsertManga(manga: Manga): MangaEntity {
	val mangaEntity = MangaEntity {
		id = manga.id
		title = manga.title.truncate(84)
		altTitle = manga.altTitle?.truncate(84)
		url = manga.url.truncate(255)
		publicUrl = manga.publicUrl.truncate(255)
		rating = manga.rating
		isNsfw = manga.isNsfw.toBoolean()
		coverUrl = manga.coverUrl.truncate(255)
		state = manga.state
		author = manga.author?.truncate(32)
		source = manga.source.truncate(32)
	}
	database.manga.addOrUpdate(mangaEntity)
	manga.tags.forEach { tag ->
		val tagEntity = TagEntity {
			id = tag.id
			title = tag.title.truncate(64)
			key = tag.key.truncate(120)
			source = tag.source.truncate(32)
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