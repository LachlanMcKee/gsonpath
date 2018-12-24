package gsonpath.generator.standard

import com.nhaarman.mockitokotlin2.mock
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.util.codeBlock
import org.junit.Assert

fun createMetadata(isRequired: Boolean) = ExtensionFieldMetadata(
        fieldInfo = mock(),
        variableName = "variable",
        jsonPath = "variable",
        isRequired = isRequired
)

fun validateCanHandleFieldRead(
        extension: GsonPathExtension,
        metadata: ExtensionFieldMetadata,
        canHandleFieldReadExpectationFunc: () -> CanHandleFieldReadExpectation) {

    val triggerFunc = {
        extension.canHandleFieldRead(mock(), metadata)
    }

    when (val expectation = canHandleFieldReadExpectationFunc()) {
        is CanHandleFieldReadExpectation.Valid -> {
            Assert.assertEquals(expectation.result, triggerFunc())
        }
        is CanHandleFieldReadExpectation.Exception -> {
            handleException(expectation.message, triggerFunc)
        }
    }
}

fun validateCodeRead(
        extension: GsonPathExtension,
        metadata: ExtensionFieldMetadata,
        checkResultIsNull: Boolean,
        codeReadExpectationFunc: () -> CodeReadExpectation) {

    val triggerFunc = {
        extension.createCodeReadResult(mock(), metadata, checkResultIsNull)
    }

    when (val expectation = codeReadExpectationFunc()) {
        is CodeReadExpectation.Valid -> {
            Assert.assertEquals(codeBlock { add(expectation.codeString) }, triggerFunc())
        }
        is CodeReadExpectation.Exception -> {
            handleException(expectation.message, triggerFunc)
        }
    }
}

fun validatePostRead(
        extension: GsonPathExtension,
        metadata: ExtensionFieldMetadata,
        postReadExpectationFunc: () -> PostReadExpectation) {

    val triggerFunc = {
        extension.createCodePostReadResult(mock(), metadata)
    }

    when (val expectation = postReadExpectationFunc()) {
        is PostReadExpectation.Valid -> {
            Assert.assertEquals(codeBlock { add(expectation.codeString) }, triggerFunc())
        }
        is PostReadExpectation.Exception -> {
            handleException(expectation.message, triggerFunc)
        }
    }
}

private fun handleException(expectedMessage: String, triggerFunc: () -> Any?) {
    var exception: ProcessingException? = null
    try {
        triggerFunc()
    } catch (e: ProcessingException) {
        exception = e
    }

    if (exception == null) {
        Assert.fail("Exception not thrown")
    } else {
        Assert.assertEquals(expectedMessage, exception.message)
    }
}

fun String.toCodeReadExpectation() = CodeReadExpectation.Valid(trimIndent())
fun String.toPostReadExpectation() = PostReadExpectation.Valid(trimIndent())

sealed class CanHandleFieldReadExpectation {
    data class Valid(val result: Boolean) : CanHandleFieldReadExpectation()
    data class Exception(val message: String) : CanHandleFieldReadExpectation()
}

sealed class CodeReadExpectation {
    data class Valid(val codeString: String) : CodeReadExpectation()
    data class Exception(val message: String) : CodeReadExpectation()
}

sealed class PostReadExpectation {
    data class Valid(val codeString: String) : PostReadExpectation()
    data class Exception(val message: String) : PostReadExpectation()
}