package org.kotatsu.model.manga

import org.ktorm.entity.Entity

interface MangaTagEntity : Entity<MangaTagEntity> {

	var manga: MangaEntity
	var tag: TagEntity

	companion object : Entity.Factory<MangaTagEntity>()
}