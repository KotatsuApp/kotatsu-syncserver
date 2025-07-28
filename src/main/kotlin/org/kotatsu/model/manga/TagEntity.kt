package org.kotatsu.model.manga

import org.ktorm.entity.Entity

interface TagEntity : Entity<TagEntity> {
	var id: Long
	var title: String
	var key: String
	var source: String

	companion object : Entity.Factory<TagEntity>()
}
