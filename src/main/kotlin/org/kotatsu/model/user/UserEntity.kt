package org.kotatsu.model.user

import org.ktorm.entity.Entity

interface UserEntity : Entity<UserEntity> {
    var id: Int
    var email: String
    var passwordHash: String
    var passwordResetTokenHash: String?
    var passwordResetTokenExpiresAt: Long?
    var nickname: String?
    var favouritesSyncTimestamp: Long?
    var historySyncTimestamp: Long?

    companion object : Entity.Factory<UserEntity>()
}
