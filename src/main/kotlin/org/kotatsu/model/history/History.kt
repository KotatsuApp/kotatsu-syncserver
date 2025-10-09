package org.kotatsu.model.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import org.kotatsu.model.manga.Manga

@Serializable
@JsonIgnoreUnknownKeys
data class History(
    @SerialName("manga_id") val mangaId: Long,
    @SerialName("manga") val manga: Manga,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long,
    @SerialName("chapter_id") val chapterId: Long,
    @SerialName("page") val page: Int,
    @SerialName("scroll") val scroll: Float,
    @SerialName("percent") val percent: Float,
    @SerialName("chapters") val chapters: Int = -1,
    @SerialName("deleted_at") var deletedAt: Long,
)
