package org.kotatsu.model.manga

enum class ContentRating {
	SAFE, SUGGESTIVE, ADULT;

	companion object {
		fun from(name: String?) = if (name.isNullOrEmpty()) {
			null
		} else {
			entries.find { x ->
				x.name == name
			}
		}
	}
}
