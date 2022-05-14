package org.kotatsu.model.favourite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.kotatsu.model.manga.Manga

@Serializable
data class Favourite(
	@SerialName("manga_id") val mangaId: Long,
	@SerialName("manga") val manga: Manga,
	@SerialName("category_id") val categoryId: Int,
	@SerialName("created_at") val createdAt: Long,
)