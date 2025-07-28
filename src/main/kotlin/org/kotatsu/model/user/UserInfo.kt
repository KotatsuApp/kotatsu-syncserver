package org.kotatsu.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
	@SerialName("id") val id: Int,
	@SerialName("email") val email: String,
	@SerialName("nickname") val nickname: String?,
)
