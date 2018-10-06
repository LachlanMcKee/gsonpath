package gsonpath.generator.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.*
import gsonpath.AutoGsonAdapter
import gsonpath.GsonUtil
import gsonpath.ProcessingException
import gsonpath.generator.HandleResult
import gsonpath.generator.adapter.read.ReadFunctions
import gsonpath.generator.adapter.subtype.SubtypeFunctions
import gsonpath.generator.adapter.write.WriteFunctions
import gsonpath.generator.writeFile
import gsonpath.util.*
import java.io.IOException
import javax.annotation.Generated
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

        val generatedJavaPoetAnnotation = AnnotationSpec.builder(Generated::class.java)
                .addMember("value", "\"gsonpath.GsonProcessor\"")
                .addMember("comments", "\"https://github.com/LachlanMcKee/gsonpath\"")
                .build()

        val metadata = adapterModelMetadataFactory.createMetadata(modelElement, autoGsonAnnotation)

        val adapterTypeBuilder = TypeSpecExt.finalClassBuilder(metadata.adapterClassName).apply {
            superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), metadata.modelClassName))
            addAnnotation(generatedJavaPoetAnnotation)

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
        }

        // Adds the mandatory field index constants and also populates the mandatoryInfoMap values.
        metadata.mandatoryInfoMap.let {
            if (it.isNotEmpty()) {
                it.values.forEachIndexed { mandatoryIndex, mandatoryField ->
                    adapterTypeBuilder.field(mandatoryField.indexVariableName, TypeName.INT) {
                        addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        initializer("" + mandatoryIndex)
                    }
                }

                adapterTypeBuilder.field("MANDATORY_FIELDS_SIZE", TypeName.INT) {
                    addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    initializer("" + it.size)
                }
            }
        }

        adapterTypeBuilder.addMethod(readFunctions.createReadMethod(metadata.readParams, extensionsHandler))

        if (!metadata.isModelInterface) {
            adapterTypeBuilder.addMethod(writeFunctions.createWriteMethod(metadata.writeParams))

        } else {
            // Create an empty method for the write, since we do not support writing for interfaces.
            adapterTypeBuilder.overrideMethod("write") {
                addParameter(JsonWriter::class.java, "out")
                addParameter(metadata.modelClassName, "value")
                addException(IOException::class.java)
            }
        }

        // Adds any required subtype type adapters depending on the usage of the GsonSubtype annotation.
        subtypeFunctions.addSubTypeTypeAdapters(adapterTypeBuilder, metadata.rootGsonObject)

        if (adapterTypeBuilder.writeFile(fileWriter, logger, metadata.adapterClassName.packageName(), this::onJavaFileBuilt)) {
            return HandleResult(metadata.modelClassName, metadata.adapterClassName)
        }

        throw ProcessingException("Failed to write generated file: " + metadata.adapterClassName.simpleName())
    }

    private fun onJavaFileBuilt(builder: JavaFile.Builder) {
        builder.addStaticImport(GsonUtil::class.java, "*")
    }
}
