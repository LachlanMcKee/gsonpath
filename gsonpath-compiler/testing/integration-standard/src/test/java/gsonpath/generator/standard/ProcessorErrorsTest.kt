package gsonpath.generator.standard

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import gsonpath.GsonProcessorImpl
import org.junit.Test

class ProcessorErrorsTest {
    @Test
    fun testInvalidFieldType() {
        val source = JavaFileObjects.forResource("generator/standard/processor_errors/TestInvalidFieldTypeError.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Invalid field type: java.lang.Object")
                .`in`(source)
                .onLine(10)
    }

    @Test
    fun testInvalidFieldPath() {
        val source = JavaFileObjects.forResource("generator/standard/processor_errors/TestInvalidFieldPathError.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Unexpected duplicate field 'value' found. Each tree branch must use a unique value!")
                .`in`(source)
                .onLine(16)
    }

    @Test
    fun testDuplicateFieldError() {
        val source = JavaFileObjects.forResource("generator/standard/processor_errors/TestDuplicateFieldError.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Unexpected duplicate field 'value' found. Each tree branch must use a unique value!")
                .`in`(source)
                .onLine(13)
    }

    @Test
    fun testUsingSerializedNameAlternate() {
        val source = JavaFileObjects.forResource("generator/standard/processor_errors/TestSerializedNameAlternateUsedError.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("SerializedName 'alternate' feature is not supported")
                .`in`(source)
                .onLine(10)
    }

    @Test
    fun testUsingFlattenJsonOnWrongField() {
        val source = JavaFileObjects.forResource("generator/standard/processor_errors/TestInvalidFlattenJsonError.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("FlattenObject can only be used on String variables")
                .`in`(source)
                .onLine(11)
    }
}
