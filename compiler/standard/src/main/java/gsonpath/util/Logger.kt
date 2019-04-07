package gsonpath.util

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.tools.Diagnostic

class Logger(private val processingEnv: ProcessingEnvironment) {

    fun printMessage(message: String) {
        println(LOG_PREFIX + message)
    }

    fun printError(message: String, element: Element? = null) {
        if (element != null) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, LOG_PREFIX + message, element)
        } else {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, LOG_PREFIX + message)
        }
    }

    companion object {
        private const val LOG_PREFIX = "Gson Path: "
    }
}