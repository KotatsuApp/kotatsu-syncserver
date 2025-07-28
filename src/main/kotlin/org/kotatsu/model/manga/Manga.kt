package org.kotatsu.model.manga

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class Manga(
	@SerialName("manga_id") val id: Long,
	@SerialName("title") val title: String,
	@SerialName("alt_title") val altTitle: String?,
	@SerialName("url") val url: String, // relative url for internal use
	@SerialName("public_url") val publicUrl: String,
	@SerialName("rating") val rating: Float, // normalized value [0..1] or -1
	@SerialName("content_rating") val contentRating: String? = null,
	@SerialName("cover_url") val coverUrl: String,
	@SerialName("large_cover_url") val largeCoverUrl: String?,
	@SerialName("tags") val tags: Set<MangaTag>,
	@SerialName("state") val state: String?,
	@SerialName("author") val author: String?,
	@SerialName("source") val source: String,
)
