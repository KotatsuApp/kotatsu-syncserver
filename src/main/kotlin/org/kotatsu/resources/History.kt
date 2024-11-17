package org.kotatsu.resources

import org.kotatsu.database
import org.kotatsu.model.history
import org.kotatsu.model.history.History
import org.kotatsu.model.history.HistoryPackage
import org.kotatsu.model.history.HistoryTable
import org.kotatsu.model.history.toHistory
import org.kotatsu.model.user.UserEntity
import org.kotatsu.util.insertOrUpdate
import org.kotatsu.util.withRetry
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.map

suspend fun syncHistory(
	user: UserEntity,
	request: HistoryPackage?,
): HistoryPackage {
	if (request != null) {
		for (history in request.history) {
			database.upsertManga(history.manga)
			database.upsertHistory(history, user.id)
		}
	}
	return HistoryPackage(
		history = database.history
			.filter { it.userId eq user.id }
			.map { it.toHistory() },
		timestamp = user.historySyncTimestamp ?: 0L,
	)
}

private suspend fun Database.upsertHistory(history: History, userId: Int) {
	val existed = this.history.find { x -> (x.manga eq history.mangaId) and (x.userId eq userId) }?.toHistory()
	if (existed == history) {
		return
	}
	withRetry {
		database.insertOrUpdate(HistoryTable, block = {
			set(it.manga, history.mangaId)
			set(it.createdAt, history.createdAt)
			set(it.updatedAt, history.updatedAt)
			set(it.chapterId, history.chapterId)
			set(it.page, history.page)
			set(it.scroll, history.scroll)
			set(it.percent, history.percent)
			set(it.chapters, history.chapters)
			set(it.deletedAt, history.deletedAt)
			set(it.userId, userId)
		}, conflictBlock = {
			set(it.createdAt, history.createdAt)
			set(it.updatedAt, history.updatedAt)
			set(it.chapterId, history.chapterId)
			set(it.page, history.page)
			set(it.scroll, history.scroll)
			set(it.percent, history.percent)
			set(it.chapters, history.chapters)
			set(it.deletedAt, history.deletedAt)
		})
	}
}