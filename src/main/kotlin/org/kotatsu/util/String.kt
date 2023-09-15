package org.kotatsu.util

import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest

fun String.md5(): String = MessageDigest.getInstance("MD5").digest(this.toByteArray(UTF_8)).decodeToString()

fun String.truncated(maxLength: Int): String = if (length > maxLength) take(maxLength) else this