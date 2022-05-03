package org.kotatsu.model.favourite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class FavouritesPackage(
	@SerialName("favourite_categories") val favouriteCategories: List<Category>,
	@SerialName("favourites") val favourites: List<Favourite>,
	@SerialName("timestamp") val timestamp: Long,
)