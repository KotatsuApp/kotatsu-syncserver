package org.kotatsu.model.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.internal.Intrinsics

@Serializable
data class HistoryPackage(
	@SerialName("history") val history: List<History>,
	@SerialName("timestamp") val timestamp: Long,
) {

	fun contentEquals(other: HistoryPackage): Boolean {
		return Intrinsics.areEqual(history, other.history)
	}
}