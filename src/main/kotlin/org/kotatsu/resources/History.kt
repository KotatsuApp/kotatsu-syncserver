package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.history
import org.kotatsu.model.history.HistoryPackage
import org.kotatsu.model.history.toEntity
import org.kotatsu.model.history.toHistory
import org.kotatsu.model.user.UserEntity
import org.kotatsu.util.addOrUpdate
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.map

fun syncHistory(
	user: UserEntity,
	request: HistoryPackage,
): HistoryPackage {
	for (history in request.history) {
		val mangaEntity = upsertManga(history.manga)
		val entity = history.toEntity(mangaEntity, user)
		database.history.addOrUpdate(entity)
	}
	return HistoryPackage(
		database.history
			.filter { it.userId eq user.id }
			.map { it.toHistory() },
		System.currentTimeMillis(),
	)
}