package org.kotatsu.model.manga

fun MangaEntity.toManga() = Manga(
	id = id,
	title = title,
	altTitle = altTitle,
	url = url,
	publicUrl = publicUrl,
	rating = rating,
	contentRating = contentRating?.name,
	coverUrl = coverUrl,
	largeCoverUrl = largeCoverUrl,
	tags = tags.mapTo(HashSet(tags.size)) { it.toTag() },
	state = state?.name,
	author = author,
	source = source,
)

fun TagEntity.toTag() = MangaTag(id, title, key, source)

fun Manga.getNsfwValue(): Boolean = if (contentRating != null) {
	contentRating == ContentRating.ADULT.name
} else {
	false
}