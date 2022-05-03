package org.kotatsu.model.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class HistoryPackage(
	@SerialName("history") val history: List<History>,
	@SerialName("timestamp") val timestamp: Long,
)