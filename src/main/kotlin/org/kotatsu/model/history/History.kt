package org.kotatsu.model.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.kotatsu.model.manga.Manga

@Serializable
data class History(
	@SerialName("manga_id") val mangaId: Long,
	@SerialName("manga") val manga: Manga,
	@SerialName("created_at") val createdAt: Long,
	@SerialName("updated_at") val updatedAt: Long,
	@SerialName("chapter_id") val chapterId: Long,
	@SerialName("page") val page: Int,
	@SerialName("scroll") val scroll: Float,
)