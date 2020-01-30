package gsonpath.integration.common

import com.google.common.truth.Truth
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourcesSubjectFactory
import gsonpath.GsonProcessor
import javax.tools.JavaFileObject

object IntegrationTester {

    fun integrationTest(vararg javaFileAndSourceType: JavaFileAndSourceType) {
        val input: List<JavaFileObject> = javaFileAndSourceType
                .filter { it.sourceType == SourceType.INPUT }
                .map { it.fileObject }

        val output: List<JavaFileObject> = javaFileAndSourceType
                .filter { it.sourceType == SourceType.OUTPUT }
                .map { it.fileObject }

        Truth.assertAbout(JavaSourcesSubjectFactory.javaSources())
                .that(input)
                .processedWith(GsonProcessor())
                .compilesWithoutError()
                .and()
                .run {
                    if (output.size == 1) {
                        generatesSources(output.first())

                    } else {
                        generatesSources(output.first(), *output.subList(1, output.size).toTypedArray())
                    }
                }
    }

    class JavaFileAndSourceType(
            val sourceType: SourceType,
            val fileObject: JavaFileObject
    )

    enum class SourceType {
        INPUT, OUTPUT
    }

    private fun source(className: String, sourceType: SourceType, foo: String): JavaFileAndSourceType {
        val trimIndent = foo.trimIndent()
        return JavaFileAndSourceType(sourceType, JavaFileObjects.forSourceString(
                "gsonpath.testing.$className", "package gsonpath.testing;\n\n$trimIndent"))
    }

    fun inputSource(className: String, foo: String): JavaFileAndSourceType {
        return source(className, SourceType.INPUT, foo)
    }

    fun outputSource(className: String, foo: String): JavaFileAndSourceType {
        return source(className, SourceType.OUTPUT, foo)
    }

    val testGsonTypeFactory = inputSource("TestGsonTypeFactory", """
        import com.google.gson.TypeAdapterFactory;
        import gsonpath.AutoGsonAdapterFactory;
    
        @AutoGsonAdapterFactory
        public interface TestGsonTypeFactory extends TypeAdapterFactory {}
        """
    )
}
