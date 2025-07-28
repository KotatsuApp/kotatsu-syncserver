package org.kotatsu.model.favourite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import org.kotatsu.model.category.Category
import kotlin.jvm.internal.Intrinsics

@Serializable
@JsonIgnoreUnknownKeys
data class FavouritesPackage(
	@SerialName("categories") val favouriteCategories: List<Category>,
	@SerialName("favourites") val favourites: List<Favourite>,
	@SerialName("timestamp") val timestamp: Long?,
) {

	fun contentEquals(other: FavouritesPackage): Boolean {
		return Intrinsics.areEqual(favouriteCategories, other.favouriteCategories) &&
			Intrinsics.areEqual(favourites, other.favourites)
	}
}
