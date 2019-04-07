package gsonpath.adapter.util

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import gsonpath.ProcessingException
import gsonpath.util.FileWriter
import java.io.IOException

fun TypeSpec.Builder.writeFile(
        fileWriter: FileWriter,
        packageName: String,
        fileBuiltFunc: (builder: JavaFile.Builder) -> Unit = {}) {

    try {
        JavaFile.builder(packageName, build()).apply {
            fileBuiltFunc(this)
            build().writeTo(fileWriter.filer)
        }

    } catch (e: IOException) {
        throw ProcessingException("Error while writing javapoet file: ${e.message}")
    }
}
