package gsonpath

import gsonpath.compiler.GsonPathExtension
import gsonpath.generator.extension.def.intdef.IntDefExtension
import gsonpath.generator.extension.def.stringdef.StringDefExtension
import gsonpath.generator.extension.empty.EmptyToNullExtension
import gsonpath.generator.extension.flatten.FlattenJsonExtension
import gsonpath.generator.extension.invalid.RemoveInvalidElementsExtension
import gsonpath.generator.extension.range.floatrange.FloatRangeExtension
import gsonpath.generator.extension.range.intrange.IntRangeExtension
import gsonpath.generator.extension.size.SizeExtension
import gsonpath.generator.extension.subtype.GsonSubTypeExtension
import gsonpath.generator.extension.subtype.SubTypeMetadataFactoryImpl
import gsonpath.util.TypeHandler
import java.util.*

object ExtensionsLoader {

    fun loadExtensions(typeHandler: TypeHandler, logger: Logger): List<GsonPathExtension> {
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

        return extensions.plus(arrayOf(
                IntDefExtension(),
                StringDefExtension(),
                EmptyToNullExtension(),
                FlattenJsonExtension(),
                RemoveInvalidElementsExtension(),
                FloatRangeExtension(),
                IntRangeExtension(),
                SizeExtension(),
                GsonSubTypeExtension(typeHandler, SubTypeMetadataFactoryImpl(typeHandler))
        ))
    }
}