package gsonpath.adapter.standard.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.squareup.javapoet.*
import gsonpath.AutoGsonAdapter
import gsonpath.GsonUtil
import gsonpath.ProcessingException
import gsonpath.adapter.Constants.GENERATED_ANNOTATION
import gsonpath.adapter.AdapterGenerationResult
import gsonpath.adapter.standard.adapter.read.ReadFunctions
import gsonpath.adapter.standard.adapter.write.WriteFunctions
import gsonpath.adapter.util.writeFile
import gsonpath.util.*
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
                    it.writeFile(fileWriter, adapterClassName.packageName(), this::onJavaFileBuilt)
                    AdapterGenerationResult(metadata.adapterGenericTypeClassNames.toTypedArray(), adapterClassName)
                }
    }

    private fun TypeSpec.Builder.addDetails(metadata: AdapterModelMetadata): TypeSpec.Builder {
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

        readFunctions.handleRead(this, metadata.readParams)
        writeFunctions.handleWrite(this, metadata.writeParams)

        return this
    }

    private fun onJavaFileBuilt(builder: JavaFile.Builder) {
        builder.addStaticImport(GsonUtil::class.java, "*")
    }
}
