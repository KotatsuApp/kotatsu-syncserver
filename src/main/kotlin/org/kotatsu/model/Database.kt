package org.kotatsu.model

import org.kotatsu.model.favourite.CategoriesTable
import org.kotatsu.model.favourite.FavouritesTable
import org.kotatsu.model.history.HistoryTable
import org.kotatsu.model.manga.MangaTable
import org.kotatsu.model.manga.MangaTagsTable
import org.kotatsu.model.manga.TagsTable
import org.kotatsu.model.user.UsersTable
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf

val Database.manga
	get() = this.sequenceOf(MangaTable)

val Database.tags
	get() = this.sequenceOf(TagsTable)

val Database.mangaTags
	get() = this.sequenceOf(MangaTagsTable)

val Database.categories
	get() = this.sequenceOf(CategoriesTable)

val Database.users
	get() = this.sequenceOf(UsersTable)

val Database.favourites
	get() = this.sequenceOf(FavouritesTable)

val Database.history
	get() = this.sequenceOf(HistoryTable)