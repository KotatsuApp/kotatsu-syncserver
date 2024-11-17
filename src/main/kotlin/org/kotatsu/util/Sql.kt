package org.kotatsu.util

import org.kotatsu.databaseType
import org.ktorm.dsl.AssignmentsBuilder
import org.ktorm.support.mysql.insertOrUpdate as mysqlInsertOrUpdate
import org.ktorm.support.postgresql.insertOrUpdate as postgresqlInsertOrUpdate

class SmartInsertBuilder<T : org.ktorm.schema.BaseTable<*>> {
	internal lateinit var insertBlock: (AssignmentsBuilder.(T) -> Unit)
	internal var conflictBlock: (AssignmentsBuilder.(T) -> Unit)? = null

	fun onInsert(block: AssignmentsBuilder.(T) -> Unit) {
		insertBlock = block
	}

	fun onDuplicateKey(block: AssignmentsBuilder.(T) -> Unit) {
		conflictBlock = block
	}
}

fun <T : org.ktorm.schema.BaseTable<*>> org.ktorm.database.Database.smartInsert(
	table: T,
	block: SmartInsertBuilder<T>.() -> Unit,
) {
	val data = SmartInsertBuilder<T>().apply(block)

	when (databaseType) {
		DatabaseType.POSTGRESQL -> postgresqlInsertOrUpdate(table) {
			data.insertBlock(this, table)
			onConflict {
				if (data.conflictBlock != null) {
					data.conflictBlock!!(this, table)
				} else {
					doNothing()
				}
			}
		}

		DatabaseType.MYSQL -> mysqlInsertOrUpdate(table) {
			data.insertBlock(this, table)
			if (data.conflictBlock != null) {
				onDuplicateKey {
					data.conflictBlock!!(this, table)
				}
				// no support for ignore insert until https://github.com/kotlin-orm/ktorm/issues/512 is merged
			}
		}
	}
}
