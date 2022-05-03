package org.kotatsu.model.manga

import org.kotatsu.database
import org.kotatsu.model.mangaTags
import org.kotatsu.util.addOrUpdate
import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import org.ktorm.entity.filter
import org.ktorm.entity.mapTo

interface MangaEntity : Entity<MangaEntity> {

	var id: Long
	var title: String
	var altTitle: String?
	var url: String
	var publicUrl: String
	var rating: Float
	var isNsfw: Boolean
	var coverUrl: String
	var largeCoverUrl: String
	var state: MangaState?
	var author: String?
	var source: String

	var tags: Collection<TagEntity>
		get() = database.mangaTags.filter {
			it.manga eq id
		}.mapTo(HashSet()) { it.tag }
		set(value) {
			for (t in value) {
				val e = MangaTagEntity {
					manga = this@MangaEntity
					tag = t
				}
				database.mangaTags.addOrUpdate(e)
			}
		}

	companion object : Entity.Factory<MangaEntity>()
}