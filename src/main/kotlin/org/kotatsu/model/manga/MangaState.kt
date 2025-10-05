package org.kotatsu.model.manga

enum class MangaState {
    ONGOING, FINISHED, ABANDONED, PAUSED, UPCOMING;

    companion object {
        fun from(name: String?) = if (name.isNullOrEmpty()) {
            null
        } else {
            MangaState.entries.find { x ->
                x.name == name
            }
        }
    }
}
