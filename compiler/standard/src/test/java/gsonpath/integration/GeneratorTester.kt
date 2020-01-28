package gsonpath.integration

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import com.google.testing.compile.ProcessedCompileTesterFactory
import gsonpath.GsonProcessor
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

                    if (generatedSources.size == 1) {
                        generatesSources(generatedSources.first())

                    } else {
                        generatesSources(generatedSources.first(),
                                *generatedSources.subList(1, generatedSources.size).toTypedArray())
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
