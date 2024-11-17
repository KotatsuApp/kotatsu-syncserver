package org.kotatsu.util

import org.kotatsu.databaseType
import org.ktorm.dsl.AssignmentsBuilder
import org.ktorm.support.mysql.insertOrUpdate as mysqlInsertOrUpdate
import org.ktorm.support.postgresql.insertOrUpdate as postgresqlInsertOrUpdate


fun <T : org.ktorm.schema.BaseTable<*>> org.ktorm.database.Database.insertOrUpdate(
	table: T,
	block: AssignmentsBuilder.(T) -> Unit,
	conflictBlock: AssignmentsBuilder.(T) -> Unit,
): Unit {
	when (databaseType) {
		DatabaseType.POSTGRESQL -> postgresqlInsertOrUpdate(table) {
			block(table)
			onConflict {
				conflictBlock(table)
			}
		}

		DatabaseType.MYSQL -> mysqlInsertOrUpdate(table) {
			block(table)
			onDuplicateKey {
				conflictBlock(table)
			}
		}
	}
}
