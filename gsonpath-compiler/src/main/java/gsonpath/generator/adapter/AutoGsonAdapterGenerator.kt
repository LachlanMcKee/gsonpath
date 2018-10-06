package gsonpath.generator.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.*
import gsonpath.AutoGsonAdapter
import gsonpath.GsonUtil
import gsonpath.ProcessingException
import gsonpath.generator.Constants.GENERATED_ANNOTATION
import gsonpath.generator.HandleResult
import gsonpath.generator.adapter.read.ReadFunctions
import gsonpath.generator.adapter.subtype.SubtypeFunctions
import gsonpath.generator.adapter.write.WriteFunctions
import gsonpath.generator.writeFile
import gsonpath.util.*
import java.io.IOException
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class AutoGsonAdapterGenerator(
        private val adapterModelMetadataFactory: AdapterModelMetadataFactory,
        private val fileWriter: FileWriter,
        private val readFunctions: ReadFunctions,
        private val writeFunctions: WriteFunctions,
        private val subtypeFunctions: SubtypeFunctions,
        private val logger: Logger) {

    @Throws(ProcessingException::class)
    fun handle(
            modelElement: TypeElement,
            autoGsonAnnotation: AutoGsonAdapter,
            extensionsHandler: ExtensionsHandler): HandleResult {

        val metadata = adapterModelMetadataFactory.createMetadata(modelElement, autoGsonAnnotation)
        val adapterClassName = metadata.adapterClassName
        return TypeSpecExt.finalClassBuilder(adapterClassName)
                .addDetails(metadata, extensionsHandler)
                .let {
                    if (it.writeFile(fileWriter, logger, adapterClassName.packageName(), this::onJavaFileBuilt)) {
                        HandleResult(metadata.modelClassName, adapterClassName)
                    } else {
                        throw ProcessingException("Failed to write generated file: " + adapterClassName.simpleName())
                    }
                }
    }

    private fun TypeSpec.Builder.addDetails(
            metadata: AdapterModelMetadata,
            extensionsHandler: ExtensionsHandler): TypeSpec.Builder {

        superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), metadata.modelClassName))
        addAnnotation(GENERATED_ANNOTATION)

        field("mGson", Gson::class.java) {
            addModifiers(Modifier.PRIVATE, Modifier.FINAL)
        }

        // Add the constructor which takes a gson instance for future use.
        constructor {
            addModifiers(Modifier.PUBLIC)
            addParameter(Gson::class.java, "gson")
            code {
                assign("this.mGson", "gson")
            }
        }

        // Adds the mandatory field index constants and also populates the mandatoryInfoMap values.
        metadata.mandatoryInfoMap.let {
            if (it.isNotEmpty()) {
                it.values.forEachIndexed { mandatoryIndex, mandatoryField ->
                    field(mandatoryField.indexVariableName, TypeName.INT) {
                        addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        initializer("" + mandatoryIndex)
                    }
                }

                field("MANDATORY_FIELDS_SIZE", TypeName.INT) {
                    addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    initializer("" + it.size)
                }
            }
        }

        addMethod(readFunctions.createReadMethod(metadata.readParams, extensionsHandler))

        if (!metadata.isModelInterface) {
            addMethod(writeFunctions.createWriteMethod(metadata.writeParams))

        } else {
            // Create an empty method for the write, since we do not support writing for interfaces.
            overrideMethod("write") {
                addParameter(JsonWriter::class.java, "out")
                addParameter(metadata.modelClassName, "value")
                addException(IOException::class.java)
            }
        }

        // Adds any required subtype type adapters depending on the usage of the GsonSubtype annotation.
        subtypeFunctions.addSubTypeTypeAdapters(this, metadata.subtypeParams)
        return this
    }

    private fun onJavaFileBuilt(builder: JavaFile.Builder) {
        builder.addStaticImport(GsonUtil::class.java, "*")
    }
}
