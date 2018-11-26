package gsonpath.generator

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import gsonpath.GsonProcessorImpl
import org.junit.Test

class ProcessorExtensionErrorsTest {
    @Test
    fun testInvalidFieldType() {
        val source = JavaFileObjects.forResource("generator/standard/processor_errors/ExtensionWithGsonSubTypeError.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("It is not possible to use extension functions with GsonSubtype")
                .`in`(source)
                .onLine(17)
    }
}
