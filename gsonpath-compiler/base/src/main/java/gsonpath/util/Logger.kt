package gsonpath.util

import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

interface Logger {
    fun error(message: String)
}

class LoggerImpl(private val processingEnvironment: ProcessingEnvironment) : Logger {
    override fun error(message: String) {
        processingEnvironment.messager.printMessage(Diagnostic.Kind.ERROR, message)
    }

}