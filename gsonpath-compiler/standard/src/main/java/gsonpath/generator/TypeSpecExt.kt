package gsonpath.generator

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import gsonpath.util.FileWriter
import gsonpath.util.Logger
import java.io.IOException

fun TypeSpec.Builder.writeFile(
        fileWriter: FileWriter,
        logger: Logger,
        packageName: String,
        fileBuiltFunc: (builder: JavaFile.Builder) -> Unit = {}): Boolean {

    return try {
        JavaFile.builder(packageName, build()).apply {
            fileBuiltFunc(this)
            build().writeTo(fileWriter.filer)
        }
        true

    } catch (e: IOException) {
        logger.error("Error while writing javapoet file: " + e.message)
        false
    }
}
