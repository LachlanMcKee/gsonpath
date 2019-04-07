package gsonpath.adapter.standard.extension

import gsonpath.ProcessingException
import gsonpath.compiler.GsonPathExtension
import gsonpath.util.Logger
import java.util.*

object ExtensionsLoader {

    fun loadExtensions(logger: Logger): List<GsonPathExtension> {
        // Load any extensions that are also available at compile time.
        println()
        val extensions: List<GsonPathExtension> =
                try {
                    ServiceLoader.load(GsonPathExtension::class.java, javaClass.classLoader).toList()

                } catch (t: Throwable) {
                    throw ProcessingException("Failed to load one or more GsonPath extensions. Cause: ${t.message}")
                }

        // Print the extensions for auditing purposes.
        extensions.forEach {
            logger.printMessage("Extension found: " + it.extensionName)
        }

        return extensions
    }
}