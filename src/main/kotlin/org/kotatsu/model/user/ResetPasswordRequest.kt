package org.kotatsu.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    @SerialName("reset_token") val resetToken: String,
    @SerialName("password") val password: String,
)
