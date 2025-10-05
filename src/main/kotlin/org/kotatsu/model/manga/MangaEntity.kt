package org.kotatsu.model.manga

import org.kotatsu.database
import org.kotatsu.model.mangaTags
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
    var contentRating: ContentRating?
    var coverUrl: String
    var largeCoverUrl: String
    var state: MangaState?
    var author: String?
    var source: String

    val tags: Collection<TagEntity>
        get() = database.mangaTags.filter {
            it.manga eq id
        }.mapTo(HashSet()) { it.tag }

    companion object : Entity.Factory<MangaEntity>()
}
