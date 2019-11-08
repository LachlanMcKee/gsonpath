package gsonpath.util

import com.sun.source.util.Trees
import javax.annotation.processing.ProcessingEnvironment

/**
 * Typically the 'com.sun.source.util.Trees' class is not allowed when making an annotation processor incremental.
 * A work around (found here: https://github.com/JakeWharton/butterknife/commit/3f675f4e23e59f645f5cecddde9f3b9fb1925cf8#diff-141a2db288c994f07eba0c49a2869e9bR155)
 * was implemented to still allow Trees to be used.
 */
class SunTreesProvider(private val env: ProcessingEnvironment) {
    private val lazyTrees: Trees by lazy {
        try {
            Trees.instance(env)
        } catch (ignored: IllegalArgumentException) {
            try {
                // Get original ProcessingEnvironment from Gradle-wrapped one or KAPT-wrapped one.
                env.javaClass.declaredFields
                        .first { field ->
                            field.name == "delegate" || field.name == "processingEnv"
                        }
                        .let { field ->
                            field.isAccessible = true
                            val javacEnv = field.get(env) as ProcessingEnvironment
                            Trees.instance(javacEnv)
                        }
            } catch (throwable: Throwable) {
                throw IllegalStateException("Unable to create Trees", throwable)
            }
        }
    }

    fun getTrees(): Trees = lazyTrees
}