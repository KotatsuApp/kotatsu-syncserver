package org.kotatsu.model.user

import org.ktorm.schema.*

object UsersTable : Table<UserEntity>("users") {
    val id = int("id").primaryKey().bindTo { it.id }
    val email = varchar("email").bindTo { it.email }
    val passwordHash = varchar("password_hash").bindTo { it.passwordHash }
    val passwordResetTokenHash = varchar("password_reset_token_hash").bindTo { it.passwordResetTokenHash }
    val passwordResetTokenExpiresAt = long("password_reset_token_expires_at").bindTo { it.passwordResetTokenExpiresAt }
    val nickname = varchar("nickname").bindTo { it.nickname }
    val favouritesSyncTimestamp = long("favourites_sync_timestamp").bindTo { it.favouritesSyncTimestamp }
    val historySyncTimestamp = long("history_sync_timestamp").bindTo { it.historySyncTimestamp }
}
