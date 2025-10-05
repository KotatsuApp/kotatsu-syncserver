package org.kotatsu.util

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory

object PasswordHasher {
    private val argon2: Argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

    private const val ITERATIONS = 3
    private const val MEMORY_KB = 65536  // 64 MB
    private const val PARALLELISM = 4

    fun hash(password: String): String =
        argon2.hash(ITERATIONS, MEMORY_KB, PARALLELISM, password.toCharArray())

    fun verify(password: String, hash: String): Boolean =
        argon2.verify(hash, password.toCharArray())

    fun isArgon2Hash(hash: String): Boolean =
        hash.startsWith("\$argon2id\$")
}
