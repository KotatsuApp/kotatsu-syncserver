package org.kotatsu.model.category

import org.kotatsu.model.user.UserEntity
import org.kotatsu.util.truncated

fun CategoryEntity.toCategory() = Category(
	id = id,
	createdAt = createdAt,
	sortKey = sortKey,
	title = title,
	order = order,
	track = track,
	showInLib = showInLib,
	deletedAt = deletedAt,
)

fun Category.toEntity(userEntity: UserEntity) = CategoryEntity {
	id = this@toEntity.id
	createdAt = this@toEntity.createdAt
	sortKey = this@toEntity.sortKey
	title = this@toEntity.title.truncated(120)
	order = this@toEntity.order
	track = this@toEntity.track
	showInLib = this@toEntity.showInLib
	deletedAt = this@toEntity.deletedAt
	user = userEntity
}
