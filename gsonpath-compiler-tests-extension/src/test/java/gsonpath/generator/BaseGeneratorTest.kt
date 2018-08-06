package gsonpath.generator

import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.ProcessedCompileTesterFactory
import gsonpath.GsonProcessorImpl

import javax.tools.JavaFileObject
import java.util.Arrays

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources

abstract class BaseGeneratorTest {

    protected fun assertGeneratedContent(criteria: TestCriteria) {
        val testerFactory: ProcessedCompileTesterFactory

        // Add all the required 'source' files.
        val sourceFilesSize = criteria.sourceFilesSize
        if (sourceFilesSize == 1) {
            testerFactory = assertAbout(javaSource()).that(criteria.getSourceFileObject(0))

        } else {
            // Since we have multiple sources, we need to use a slightly different assert.
            val sources = (0..sourceFilesSize - 1).map { criteria.getSourceFileObject(it) }
            testerFactory = assertAbout(javaSources()).that(sources)
        }

        val predicateClause = testerFactory.processedWith(GsonProcessorImpl())
                .compilesWithoutError()
                .and()

        // Add all the required 'generated' files based off the input source files.
        val generatedFilesSize = criteria.generatedFilesSize
        val generatedSources = arrayOfNulls<JavaFileObject>(generatedFilesSize)
        for (i in 0..generatedFilesSize - 1) {
            generatedSources[i] = criteria.getGeneratedFileObject(i)
        }

        if (generatedSources.size == 1) {
            predicateClause.generatesSources(generatedSources[0])

        } else {
            predicateClause.generatesSources(generatedSources[0], *Arrays.copyOfRange<JavaFileObject>(generatedSources, 1, generatedSources.size))
        }
    }

    data class TestCriteria(private val resourcePath: String,
                            val relativeSourceNames: List<String> = emptyList(),
                            val relativeGeneratedNames: List<String> = emptyList(),
                            val absoluteSourceNames: List<String> = emptyList(),
                            val absoluteGeneratedNames: List<String> = emptyList()) {

        internal val sourceFilesSize: Int
            get() = relativeSourceNames.size + absoluteSourceNames.size

        internal val generatedFilesSize: Int
            get() = relativeGeneratedNames.size + absoluteGeneratedNames.size

        internal fun getSourceFileObject(index: Int): JavaFileObject {
            val relativeSize = relativeSourceNames.size
            if (index < relativeSize) {
                return JavaFileObjects.forResource(resourcePath + "/" + relativeSourceNames[index])
            }
            return JavaFileObjects.forResource(absoluteSourceNames[index - relativeSize])
        }

        internal fun getGeneratedFileObject(index: Int): JavaFileObject {
            val relativeSize = relativeGeneratedNames.size
            if (index < relativeSize) {
                return JavaFileObjects.forResource(resourcePath + "/" + relativeGeneratedNames[index])
            }
            return JavaFileObjects.forResource(absoluteGeneratedNames[index - relativeSize])
        }

    }

}
