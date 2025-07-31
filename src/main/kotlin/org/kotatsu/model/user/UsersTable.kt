package org.kotatsu.model.user

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object UsersTable : Table<UserEntity>("users") {
	val id = int("id").primaryKey().bindTo { it.id }
	val email = varchar("email").bindTo { it.email }
	val passwordHash = varchar("password_hash").bindTo { it.passwordHash }
	val nickname = varchar("nickname").bindTo { it.nickname }
	val favouritesSyncTimestamp = long("favourites_sync_timestamp").bindTo { it.favouritesSyncTimestamp }
	val historySyncTimestamp = long("history_sync_timestamp").bindTo { it.historySyncTimestamp }
}
