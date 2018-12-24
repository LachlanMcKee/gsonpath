package gsonpath.compiler

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec

import javax.annotation.processing.ProcessingEnvironment

/**
 * An extension to the Gsonpath annotation processor.
 *
 * It allows external libraries to add extra code to the standard generated TypeAdapter.
 *
 * Extensions are discovered at compile time using the [java.util.ServiceLoader] APIs.
 *
 * To ensure that the extension is discovered the extension must do the following:
 *  * Implement this interface
 *  * Ensure that a public no-arg constructor is available
 *  * Annotate the class with the '@AutoService' annotation provided by 'com.google.auto.service' library
 */
interface GsonPathExtension {
    /**
     * The name of the extension that will be written in the generated TypeAdapter.
     *
     * @return a non-null name that represents the extension's responsibilities.
     */
    val extensionName: String

    /**
     * Creates a code block that executes any field validation that the implementing extension wishes. This block of
     * code will be inserted after a variable has been assigned within the 'read' method of the TypeAdapter.
     *
     * Typically each extension should be used to handle a single annotation, however this is at the discression of
     * the implementing library.
     *
     * Note: The parent processor will wrap this code block with a null-safety check if it is not a primitive value,
     * therefore the code block being generated does not need to implement this behaviour.
     *
     * @param processingEnvironment  exposes facilities provided by the annotation processor framework.
     * @param extensionFieldMetadata metadata about the field being read by the parent processor.
     * @return a code block if applicable, can be null.
     */
    fun canHandleFieldRead(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean = false

    fun canHandleFieldWrite(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean = false

    fun createCodeReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata,
            checkIfResultIsNull: Boolean): ExtensionResult {

        throw UnsupportedOperationException("This extension does not handle reading")
    }

    fun createCodeWriteResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata,
            checkIfResultIsNull: Boolean): ExtensionResult {

        throw UnsupportedOperationException("This extension does not handle reading")
    }

    fun createCodePostReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): ExtensionResult? = null

    data class ExtensionResult(
            val codeBlock: CodeBlock,
            val fieldSpecs: List<FieldSpec> = emptyList(),
            val typeSpecs: List<TypeSpec> = emptyList(),
            val methodSpecs: List<MethodSpec> = emptyList()
    )
}
