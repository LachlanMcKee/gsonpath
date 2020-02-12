package gsonpath.integration.common

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.CompileTester
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import com.google.testing.compile.ProcessedCompileTesterFactory
import gsonpath.GsonProcessor
import java.io.File
import javax.tools.JavaFileObject

object GeneratorTester {

    fun assertGeneratedContent(criteria: TestCriteria, vararg options: String) {
        val sourceFilesSize = criteria.absoluteSourceNames.size + criteria.relativeSourceNames.size

        // Add all the required 'source' files.
        val testerFactory: ProcessedCompileTesterFactory = if (sourceFilesSize == 1) {
            assertAbout(javaSource()).that(getSourceFileObject(criteria, 0))

        } else {
            // Since we have multiple sources, we need to use a slightly different assert.
            val sources = (0 until sourceFilesSize).map { getSourceFileObject(criteria, it) }
            assertAbout(javaSources()).that(sources)
        }

        testerFactory
                .withCompilerOptions(*options)
                .processedWith(GsonProcessor())
                .compilesWithoutError()
                .and()
                .apply {
                    // Add all the required 'generated' files based off the input source files.
                    val generatedSources = criteria.relativeGeneratedNames.indices.map {
                        getGeneratedFileObject(criteria, it)
                    }

                    assertGeneratedSources(generatedSources)
                    // updateGeneratedSources(criteria, generatedSources)
                }
    }

    private fun <T> CompileTester.GeneratedPredicateClause<T>.assertGeneratedSources(
            generatedSources: List<JavaFileObject>
    ) {
        if (generatedSources.size == 1) {
            generatesSources(generatedSources.first())

        } else {
            generatesSources(generatedSources.first(),
                    *generatedSources.subList(1, generatedSources.size).toTypedArray())
        }
    }

    private fun <T> CompileTester.GeneratedPredicateClause<T>.updateGeneratedSources(
            criteria: TestCriteria,
            generatedSources: List<JavaFileObject>
    ) {
        try {
            if (generatedSources.size == 1) {
                generatesSources(generatedSources.first())

            } else {
                generatesSources(generatedSources.first(),
                        *generatedSources.subList(1, generatedSources.size).toTypedArray())
            }
        } catch (e: Throwable) {
            val actualSources = e.message!!.split("Actual Source:")

            val sourcesToReplace = criteria.relativeGeneratedNames
                    .mapNotNull { generatedName ->
                        val nameToFind = "class ${generatedName.removeSuffix(".java")}"
                        println("nameToFind=$nameToFind")
                        actualSources
                                .filter { source -> source.contains(nameToFind) }
                                .let {
                                    println("size: ${it.size}")
                                    if (it.size > 1) {
                                        it[1]
                                    } else {
                                        null
                                    }
                                }
                                ?.let { generatedName to it }
                    }

            sourcesToReplace.forEach { (generatedName: String, source: String) ->
                val messageP1 = source.split("package ")[1]
                val messageP2 = messageP1.substring(0, messageP1.indexOfLast { it == '}' } + 1)

                println("Updating $generatedName. src/test/resources/${criteria.resourcePath}/$generatedName")
                println("package $messageP2")
                File("src/test/resources/${criteria.resourcePath}/$generatedName")
                        .delete()
                File("src/test/resources/${criteria.resourcePath}/$generatedName")
                        .writeText("package $messageP2")
            }
        }
    }

    private fun getSourceFileObject(criteria: TestCriteria, index: Int): JavaFileObject {
        return criteria.let {
            val relativeSize = it.relativeSourceNames.size
            if (index < relativeSize) {
                JavaFileObjects.forResource(it.resourcePath + "/" + it.relativeSourceNames[index])
            } else {
                JavaFileObjects.forResource(it.absoluteSourceNames[index - relativeSize])
            }
        }
    }

    private fun getGeneratedFileObject(criteria: TestCriteria, index: Int): JavaFileObject {
        return criteria.let {
            JavaFileObjects.forResource(it.resourcePath + "/" + it.relativeGeneratedNames[index])
        }
    }
}
