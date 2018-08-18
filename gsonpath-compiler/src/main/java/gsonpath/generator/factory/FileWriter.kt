package gsonpath.generator.factory

import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment

class FileWriter(private val processingEnvironment: ProcessingEnvironment) {
    val filer: Filer
        get() = processingEnvironment.filer
}