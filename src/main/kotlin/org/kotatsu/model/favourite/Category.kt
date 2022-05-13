package org.kotatsu.model.favourite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Category(
	@SerialName("category_id") val id: Int,
	@SerialName("created_at") var createdAt: Long,
	@SerialName("sort_key") var sortKey: Int,
	@SerialName("track") var track: Int,
	@SerialName("title") var title: String,
	@SerialName("order") var order: String,
)