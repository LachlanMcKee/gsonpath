package gsonpath.generator.standard

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.*
import com.google.testing.compile.CompileTester.GeneratedPredicateClause
import com.google.testing.compile.CompileTester.SuccessfulCompilationClause
import gsonpath.GsonProcessor
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import javax.tools.JavaFileObject
import javax.tools.StandardLocation

class CustomAutoGsonAdapterTest {
    @JvmField
    @Rule
    val expectedException: ExpectedException = ExpectedException.none()

    private fun standardGeneratorTester(relativePath: String): GeneratorTester {
        return GeneratorTester(relativePath)
                .absoluteFiles("generator/standard/TestGsonTypeFactory.java")
    }

    @Test
    fun testCustomAutoGsonAdapterAnnotationWithoutIncrementalProcessing() {
        standardGeneratorTester("generator/standard/custom_adapter_annotation")
                .relativeFiles("CustomAutoGsonAdapter.java", "TestCustomAutoGsonAdapterModel.java")
                .compilesWithoutError { relativePath ->
                    generatesFiles("$relativePath/TestCustomAutoGsonAdapterModel_GsonTypeAdapter.java")
                }
    }

    @Test
    fun testCustomAutoGsonAdapterAnnotationWithIncrementalProcessingAndAdditionalAnnotations() {
        standardGeneratorTester("generator/standard/custom_adapter_annotation")
                .relativeFiles("CustomAutoGsonAdapter.java", "TestCustomAutoGsonAdapterModel.java")
                .isIncremental(true)
                .additionalAnnotations("generator.standard.custom_adapter_annotation.CustomAutoGsonAdapter")
                .compilesWithoutError { relativePath ->
                    generatesFiles("$relativePath/TestCustomAutoGsonAdapterModel_GsonTypeAdapter.java")
                }
    }

    @Test
    fun testCustomAutoGsonAdapterAnnotationWithIncrementalProcessingAndNoAdditionalAnnotations() {
        expectedException.expect(AssertionError::class.java)
        expectedException.expectMessage(CoreMatchers.containsString("expected to generate file: " +
                "/generator/standard/custom_adapter_annotation/TestCustomAutoGsonAdapterModel_GsonTypeAdapter.java"))

        standardGeneratorTester("generator/standard/custom_adapter_annotation")
                .relativeFiles("CustomAutoGsonAdapter.java", "TestCustomAutoGsonAdapterModel.java")
                .isIncremental(true)
                .compilesWithoutError {
                    generatesFileNamed(StandardLocation.SOURCE_OUTPUT,
                            "generator.standard.custom_adapter_annotation",
                            "TestCustomAutoGsonAdapterModel_GsonTypeAdapter.java")
                }
    }
}

class GeneratorTester(private val relativePath: String) {
    private val relativeFiles = mutableListOf<JavaFileObject>()
    private val absoluteFiles = mutableListOf<JavaFileObject>()
    private val additionalAnnotations = mutableListOf<String>()
    private var incremental = false

    fun relativeFiles(vararg files: String): GeneratorTester {
        files.forEach { relativeFiles.add(JavaFileObjects.forResource("$relativePath/$it")) }
        return this
    }

    fun absoluteFiles(vararg files: String): GeneratorTester {
        files.forEach { absoluteFiles.add(JavaFileObjects.forResource(it)) }
        return this
    }

    fun isIncremental(incremental: Boolean): GeneratorTester {
        this.incremental = incremental
        return this
    }

    fun additionalAnnotations(vararg annotations: String): GeneratorTester {
        annotations.forEach { additionalAnnotations.add(it) }
        return this
    }

    private fun buildTester(): CompileTester {
        val combinedList = relativeFiles.plus(absoluteFiles)
        val compileTesterFactory: ProcessedCompileTesterFactory = if (combinedList.size == 1) {
            assertAbout(JavaSourceSubjectFactory.javaSource()).that(combinedList.first())

        } else {
            // Since we have multiple sources, we need to use a slightly different assert.
            assertAbout(JavaSourcesSubjectFactory.javaSources()).that(combinedList)
        }
        return compileTesterFactory
                .let {
                    if (incremental) {
                        it.withCompilerOptions("-Agsonpath.incremental=true")
                    } else {
                        it
                    }
                }
                .let {
                    if (additionalAnnotations.size > 0) {
                        it.withCompilerOptions("-Agsonpath.additionalAnnotations=${additionalAnnotations.joinToString()}")
                    } else {
                        it
                    }
                }
                .processedWith(GsonProcessor())
    }

    fun compilesWithoutError(func: GeneratedPredicateClause<SuccessfulCompilationClause>.(String) -> Unit) {
        func(buildTester().compilesWithoutError().and(), relativePath)
    }

    fun failsToCompile(): CompileTester.UnsuccessfulCompilationClause {
        return buildTester().failsToCompile()
    }
}

fun <T> GeneratedPredicateClause<T>.generatesFiles(vararg files: String): T {
    val generatedSources = files.map { JavaFileObjects.forResource(it) }
    return if (files.size == 1) {
        generatesSources(generatedSources.first())

    } else {
        generatesSources(generatedSources.first(),
                *generatedSources.subList(1, generatedSources.size).toTypedArray())
    }
}