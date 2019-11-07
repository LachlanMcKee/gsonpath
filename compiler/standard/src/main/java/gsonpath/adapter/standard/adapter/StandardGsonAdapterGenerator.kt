package gsonpath.adapter.standard.adapter

import com.google.gson.Gson
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import gsonpath.AutoGsonAdapter
import gsonpath.GsonPathTypeAdapter
import gsonpath.ProcessingException
import gsonpath.adapter.AdapterGenerationResult
import gsonpath.adapter.Constants.GENERATED_ANNOTATION
import gsonpath.adapter.standard.adapter.read.ReadFunctions
import gsonpath.adapter.standard.adapter.write.WriteFunctions
import gsonpath.adapter.util.writeFile
import gsonpath.util.FileWriter
import gsonpath.util.TypeSpecExt
import gsonpath.util.constructor
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class StandardGsonAdapterGenerator(
        private val adapterModelMetadataFactory: AdapterModelMetadataFactory,
        private val fileWriter: FileWriter,
        private val readFunctions: ReadFunctions,
        private val writeFunctions: WriteFunctions) {

    @Throws(ProcessingException::class)
    fun handle(
            modelElement: TypeElement,
            autoGsonAnnotation: AutoGsonAdapter): AdapterGenerationResult {

        val metadata = adapterModelMetadataFactory.createMetadata(modelElement, autoGsonAnnotation)
        val adapterClassName = metadata.adapterClassName
        return TypeSpecExt.finalClassBuilder(adapterClassName)
                .addDetails(metadata)
                .let {
                    it.writeFile(fileWriter, adapterClassName.packageName())
                    AdapterGenerationResult(metadata.adapterGenericTypeClassNames.toTypedArray(), adapterClassName)
                }
    }

    private fun TypeSpec.Builder.addDetails(metadata: AdapterModelMetadata): TypeSpec.Builder {
        superclass(ParameterizedTypeName.get(ClassName.get(GsonPathTypeAdapter::class.java), metadata.modelClassName))
        addAnnotation(GENERATED_ANNOTATION)

        // Add the constructor which takes a gson instance for future use.
        constructor {
            addModifiers(Modifier.PUBLIC)
            addParameter(Gson::class.java, "gson")
            addStatement("super(gson)")
        }

        readFunctions.handleRead(this, metadata.readParams)
        writeFunctions.handleWrite(this, metadata.writeParams)

        return this
    }
}
