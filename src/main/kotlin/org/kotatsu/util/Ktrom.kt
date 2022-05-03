package org.kotatsu.util

import org.ktorm.entity.Entity
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.add
import org.ktorm.entity.update
import org.ktorm.schema.Table

fun <E : Entity<E>, T : Table<E>> EntitySequence<E, T>.addOrUpdate(entity: E): Boolean {
	val updateResult = update(entity)
	if (updateResult > 0) {
		return true
	}
	return runCatching {
		add(entity)
	}.onFailure {
		// TODO
	}.isSuccess
}