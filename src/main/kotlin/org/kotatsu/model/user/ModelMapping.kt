package org.kotatsu.model.user

fun UserEntity.toUserInfo() = UserInfo(
    id = id,
    email = email,
    nickname = nickname,
)
