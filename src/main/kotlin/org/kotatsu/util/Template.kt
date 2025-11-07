package org.kotatsu.util

import org.kotatsu.mustacheFactory
import java.io.StringWriter

fun renderTemplateToString(templatePath: String, data: Map<String, Any>): String {
    val writer = StringWriter()
    val mustache = mustacheFactory.compile(templatePath)
    mustache.execute(writer, data).flush()
    return writer.toString()
}
