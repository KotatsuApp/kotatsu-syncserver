package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.manga
import org.kotatsu.model.manga.Manga
import org.kotatsu.model.manga.MangaEntity
import org.kotatsu.model.manga.TagEntity
import org.kotatsu.util.addOrUpdate
import org.kotatsu.util.toBoolean

fun upsertManga(mangaList: Iterable<Manga>): List<MangaEntity> {
	return database.useTransaction {
		mangaList.map { manga ->
			upsertManga(manga)
		}
	}
}

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
		tags = manga.tags.map { tag ->
			TagEntity {
				id = tag.id
				title = tag.title
				key = tag.key
				source = tag.source
			}
		}
	}
	database.manga.addOrUpdate(mangaEntity)
	return mangaEntity
}