package org.kotatsu.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
	@SerialName("email") val email: String,
	@SerialName("password") val password: String,
)