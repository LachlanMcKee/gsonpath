package gsonpath.generator

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