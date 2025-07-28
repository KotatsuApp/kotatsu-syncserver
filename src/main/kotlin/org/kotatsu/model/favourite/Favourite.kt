package org.kotatsu.model.favourite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import org.kotatsu.model.manga.Manga

@Serializable
@JsonIgnoreUnknownKeys
data class Favourite(
	@SerialName("manga_id") val mangaId: Long,
	@SerialName("manga") val manga: Manga,
	@SerialName("category_id") val categoryId: Int,
	@SerialName("sort_key") val sortKey: Int,
	@SerialName("pinned") val pinned: Boolean,
	@SerialName("created_at") val createdAt: Long,
	@SerialName("deleted_at") var deletedAt: Long,
)
