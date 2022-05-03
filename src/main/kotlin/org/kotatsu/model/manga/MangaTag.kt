package org.kotatsu.model.manga

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MangaTag(
	@SerialName("tag_id") val id: Long,
	@SerialName("title") val title: String,
	@SerialName("key") val key: String,
	@SerialName("source") val source: String,
)