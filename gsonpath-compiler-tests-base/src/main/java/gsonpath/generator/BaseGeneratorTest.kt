package gsonpath.generator

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import com.google.testing.compile.ProcessedCompileTesterFactory
import gsonpath.GsonProcessorImpl
import javax.tools.JavaFileObject

abstract class BaseGeneratorTest {

    protected fun assertGeneratedContent(criteria: TestCriteria) {
        val sourceFilesSize = criteria.sourceFilesSize

        // Add all the required 'source' files.
        val testerFactory: ProcessedCompileTesterFactory = if (sourceFilesSize == 1) {
            assertAbout(javaSource()).that(criteria.getSourceFileObject(0))

        } else {
            // Since we have multiple sources, we need to use a slightly different assert.
            val sources = (0 until sourceFilesSize).map { criteria.getSourceFileObject(it) }
            assertAbout(javaSources()).that(sources)
        }

        testerFactory.processedWith(GsonProcessorImpl())
                .compilesWithoutError()
                .and()
                .apply {
                    // Add all the required 'generated' files based off the input source files.
                    val generatedSources = (0 until criteria.generatedFilesSize).map {
                        criteria.getGeneratedFileObject(it)
                    }

                    if (generatedSources.size == 1) {
                        generatesSources(generatedSources.first())

                    } else {
                        generatesSources(generatedSources.first(),
                                *generatedSources.subList(1, generatedSources.size).toTypedArray())
                    }
                }
    }

    class TestCriteria(private val resourcePath: String,
                       val relativeSourceNames: List<String> = emptyList(),
                       val relativeGeneratedNames: List<String> = emptyList(),
                       val absoluteSourceNames: List<String> = emptyList(),
                       private val absoluteGeneratedNames: List<String> = emptyList()) {

        val sourceFilesSize: Int
            get() = relativeSourceNames.size + absoluteSourceNames.size

        val generatedFilesSize: Int
            get() = relativeGeneratedNames.size + absoluteGeneratedNames.size

        fun getSourceFileObject(index: Int): JavaFileObject {
            val relativeSize = relativeSourceNames.size
            if (index < relativeSize) {
                return JavaFileObjects.forResource(resourcePath + "/" + relativeSourceNames[index])
            }
            return JavaFileObjects.forResource(absoluteSourceNames[index - relativeSize])
        }

        fun getGeneratedFileObject(index: Int): JavaFileObject {
            val relativeSize = relativeGeneratedNames.size
            if (index < relativeSize) {
                return JavaFileObjects.forResource(resourcePath + "/" + relativeGeneratedNames[index])
            }
            return JavaFileObjects.forResource(absoluteGeneratedNames[index - relativeSize])
        }
    }
}
