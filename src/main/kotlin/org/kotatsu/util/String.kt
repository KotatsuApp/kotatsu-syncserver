package org.kotatsu.util

import java.math.BigInteger
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest

fun String.md5(): String =
	BigInteger(1, MessageDigest.getInstance("MD5").digest(this.toByteArray(UTF_8))).toString(16).padStart(32, '0')

fun String.truncated(maxLength: Int): String = if (length > maxLength) take(maxLength) else this