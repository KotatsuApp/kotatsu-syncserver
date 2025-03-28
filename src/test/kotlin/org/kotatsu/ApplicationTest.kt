package org.kotatsu

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
	@Test
	fun testRoot() = testApplication {
		client.get("/").apply {
			assertEquals(HttpStatusCode.OK, status)
			assertEquals("Alive", bodyAsText())
		}
	}
}