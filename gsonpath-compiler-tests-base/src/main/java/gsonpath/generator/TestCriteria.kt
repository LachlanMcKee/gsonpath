package gsonpath.generator

import com.google.testing.compile.JavaFileObjects
import javax.tools.JavaFileObject

class TestCriteria(val resourcePath: String,
                   val relativeSourceNames: List<String> = emptyList(),
                   val relativeGeneratedNames: List<String> = emptyList(),
                   val absoluteSourceNames: List<String> = emptyList(),
                   val absoluteGeneratedNames: List<String> = emptyList()) {

    val sourceFilesSize: Int
        get() = relativeSourceNames.size + absoluteSourceNames.size

    val generatedFilesSize: Int
        get() = relativeGeneratedNames.size + absoluteGeneratedNames.size
}