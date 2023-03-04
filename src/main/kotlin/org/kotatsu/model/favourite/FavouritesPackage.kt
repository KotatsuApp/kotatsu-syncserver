package org.kotatsu.model.favourite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.internal.Intrinsics

@Serializable
data class FavouritesPackage(
	@SerialName("favourite_categories") val favouriteCategories: List<Category>,
	@SerialName("favourites") val favourites: List<Favourite>,
	@SerialName("timestamp") val timestamp: Long?,
) {

	fun contentEquals(other: FavouritesPackage): Boolean {
		return Intrinsics.areEqual(favouriteCategories, other.favouriteCategories) &&
			Intrinsics.areEqual(favourites, other.favourites)
	}
}