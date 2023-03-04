package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.history
import org.kotatsu.model.history.HistoryPackage
import org.kotatsu.model.history.HistoryTable
import org.kotatsu.model.history.toHistory
import org.kotatsu.model.user.UserEntity
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.map
import org.ktorm.support.mysql.insertOrUpdate

fun syncHistory(
	user: UserEntity,
	request: HistoryPackage?,
): HistoryPackage {
	if (request != null) {
		for (history in request.history) {
			database.upsertManga(history.manga)
			database.insertOrUpdate(HistoryTable) {
				set(it.manga, history.mangaId)
				set(it.createdAt, history.createdAt)
				set(it.updatedAt, history.updatedAt)
				set(it.chapterId, history.chapterId)
				set(it.page, history.page)
				set(it.scroll, history.scroll)
				set(it.percent, history.percent)
				set(it.deletedAt, history.deletedAt)
				set(it.userId, user.id)
				onDuplicateKey {
					set(it.createdAt, history.createdAt)
					set(it.updatedAt, history.updatedAt)
					set(it.chapterId, history.chapterId)
					set(it.page, history.page)
					set(it.scroll, history.scroll)
					set(it.percent, history.percent)
					set(it.deletedAt, history.deletedAt)
				}
			}
		}
	}
	return HistoryPackage(
		history = database.history
			.filter { it.userId eq user.id }
			.map { it.toHistory() },
		timestamp = user.historySyncTimestamp ?: 0L,
	)
}