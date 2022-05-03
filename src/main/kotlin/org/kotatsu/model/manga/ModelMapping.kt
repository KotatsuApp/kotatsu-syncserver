package org.kotatsu.model.manga

import org.kotatsu.util.toInt

fun MangaEntity.toManga() = Manga(
	id = id,
	title = title,
	altTitle = altTitle,
	url = url,
	publicUrl = publicUrl,
	rating = rating,
	isNsfw = isNsfw.toInt(),
	coverUrl = coverUrl,
	largeCoverUrl = largeCoverUrl,
	tags = tags.mapTo(HashSet()) { it.toTag() },
	state = state,
	author = author,
	source = source,
)

fun TagEntity.toTag() = MangaTag(id, title, key, source)