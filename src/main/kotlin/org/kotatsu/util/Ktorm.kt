package org.kotatsu.util

import com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException
import kotlinx.coroutines.yield
import org.ktorm.entity.Entity
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.add
import org.ktorm.entity.update
import org.ktorm.schema.Table
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException

@Deprecated("")
fun <E : Entity<E>, T : Table<E>> EntitySequence<E, T>.addOrUpdate(entity: E): Boolean {
	val updateResult = update(entity)
	if (updateResult > 0) {
		return true
	}
	return runCatching {
		add(entity)
	}.onFailure {
		if (it.message?.startsWith("Duplicate entry") != true) { // TODO fix
			it.printStackTrace()
		}
	}.isSuccess
}

suspend fun <R> withRetry(body: suspend () -> R): R {
	var error: SQLException? = null
	repeat(4) {
		try {
			return body()
		} catch (e: MySQLTransactionRollbackException) {
			error = e
			yield()
		} catch (e: SQLIntegrityConstraintViolationException) {
			error = e
			yield()
		}
	}
	throw checkNotNull(error)
}
