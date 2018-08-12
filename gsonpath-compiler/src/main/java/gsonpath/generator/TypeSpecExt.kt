package gsonpath.generator

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec

import java.io.IOException

import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

fun TypeSpec.Builder.writeFile(processingEnv: ProcessingEnvironment,
                               packageName: String,
                               fileBuiltFunc: (builder: JavaFile.Builder) -> Unit = {}): Boolean {

    return try {
        JavaFile.builder(packageName, build()).apply {
            fileBuiltFunc(this)
            build().writeTo(processingEnv.filer)
        }
        true

    } catch (e: IOException) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Error while writing javapoet file: " + e.message)
        false
    }
}
