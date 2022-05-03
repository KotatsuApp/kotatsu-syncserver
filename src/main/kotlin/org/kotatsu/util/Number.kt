package org.kotatsu.util

fun Number.toBoolean(): Boolean {
	return this.toInt() > 0
}

fun Boolean.toInt(): Int {
	return if (this) 1 else 0
}