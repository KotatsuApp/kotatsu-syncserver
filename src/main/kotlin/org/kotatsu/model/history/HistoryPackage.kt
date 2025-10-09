package org.kotatsu.model.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.jvm.internal.Intrinsics

@Serializable
@JsonIgnoreUnknownKeys
data class HistoryPackage(
    @SerialName("history") val history: List<History>,
    @SerialName("timestamp") val timestamp: Long?,
) {
    fun contentEquals(other: HistoryPackage): Boolean {
        return Intrinsics.areEqual(history, other.history)
    }
}
