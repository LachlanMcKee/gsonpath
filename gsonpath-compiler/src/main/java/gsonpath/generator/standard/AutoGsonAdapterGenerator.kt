package gsonpath.generator.standard

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.*
import gsonpath.AutoGsonAdapter
import gsonpath.GsonUtil
import gsonpath.ProcessingException
import gsonpath.compiler.generateClassName
import gsonpath.generator.HandleResult
import gsonpath.generator.interf.ModelInterfaceGenerator
import gsonpath.generator.standard.properties.AutoGsonAdapterPropertiesFactory
import gsonpath.generator.standard.read.ReadFunctions
import gsonpath.generator.standard.read.ReadParams
import gsonpath.generator.standard.subtype.SubtypeFunctions
import gsonpath.generator.standard.write.WriteFunctions
import gsonpath.generator.writeFile
import gsonpath.model.*
import gsonpath.util.*
import java.io.IOException
import javax.annotation.Generated
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType

class AutoGsonAdapterGenerator(
        private val fieldInfoFactory: FieldInfoFactory,
        private val typeHandler: TypeHandler,
        private val fileWriter: FileWriter,
        private val gsonObjectTreeFactory: GsonObjectTreeFactory,
        private val readFunctions: ReadFunctions,
        private val writeFunctions: WriteFunctions,
        private val subtypeFunctions: SubtypeFunctions,
        private val logger: Logger) {

    @Throws(ProcessingException::class)
    fun handle(
            modelElement: TypeElement,
            autoGsonAnnotation: AutoGsonAdapter,
            extensionsHandler: ExtensionsHandler): HandleResult {

        val modelClassName = ClassName.get(modelElement)
        val adapterClassName = ClassName.get(modelClassName.packageName(),
                generateClassName(modelClassName, "GsonTypeAdapter"))

        val generatedJavaPoetAnnotation = AnnotationSpec.builder(Generated::class.java)
                .addMember("value", "\"gsonpath.GsonProcessor\"")
                .addMember("comments", "\"https://github.com/LachlanMcKee/gsonpath\"")
                .build()

        val adapterTypeBuilder = TypeSpecExt.finalClassBuilder(adapterClassName).apply {
            superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), modelClassName))
            addAnnotation(generatedJavaPoetAnnotation)
            addField(Gson::class.java, "mGson", Modifier.PRIVATE, Modifier.FINAL)

            // Add the constructor which takes a gson instance for future use.
            constructor {
                addModifiers(Modifier.PUBLIC)
                addParameter(Gson::class.java, "gson")
                addStatement("this.\$N = \$N", "mGson", "gson")
            }
        }

        val concreteClassName: ClassName
        val fieldInfoList: List<FieldInfo>
        val isModelInterface = modelElement.kind.isInterface

        val properties = AutoGsonAdapterPropertiesFactory().create(autoGsonAnnotation, isModelInterface)

        val requiresConstructorInjection: Boolean =
                if (isModelInterface) {
                    true
                } else {
                    findNonEmptyConstructor(modelElement) != null
                }

        if (!isModelInterface) {
            concreteClassName = modelClassName

            fieldInfoList = fieldInfoFactory.getModelFieldsFromElement(
                    modelElement,
                    properties.fieldsRequireAnnotation,
                    requiresConstructorInjection)

        } else {
            val interfaceInfo = ModelInterfaceGenerator(typeHandler, fileWriter, logger).handle(modelElement)
            concreteClassName = interfaceInfo.parentClassName

            fieldInfoList = fieldInfoFactory.getModelFieldsFromInterface(interfaceInfo)
        }

        val rootGsonObject = gsonObjectTreeFactory
                .createGsonObject(fieldInfoList, properties.rootField,
                        GsonObjectMetadata(properties.flattenDelimiter,
                                properties.gsonFieldNamingPolicy,
                                properties.gsonFieldValidationType,
                                properties.pathSubstitutions))

        // Adds the mandatory field index constants and also populates the mandatoryInfoMap values.
        val mandatoryInfoMap = MandatoryFieldInfoFactory().createMandatoryFieldsFromGsonObject(rootGsonObject)
        if (mandatoryInfoMap.isNotEmpty()) {
            mandatoryInfoMap.values
                    .mapIndexed { mandatoryIndex, mandatoryField ->
                        FieldSpec.builder(TypeName.INT, mandatoryField.indexVariableName).applyAndBuild {
                            addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                            initializer("" + mandatoryIndex)
                        }
                    }
                    .forEach { adapterTypeBuilder.addField(it) }

            adapterTypeBuilder.addField(FieldSpec.builder(TypeName.INT, "MANDATORY_FIELDS_SIZE").applyAndBuild {
                addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                initializer("" + mandatoryInfoMap.size)
            })
        }

        val readParams = ReadParams(
                baseElement = modelClassName,
                concreteElement = concreteClassName,
                requiresConstructorInjection = requiresConstructorInjection,
                mandatoryInfoMap = mandatoryInfoMap,
                rootElements = rootGsonObject,
                flattenedFields = gsonObjectTreeFactory.getFlattenedFieldsFromGsonObject(rootGsonObject))

        adapterTypeBuilder.addMethod(readFunctions.createReadMethod(readParams, extensionsHandler))

        if (!isModelInterface) {
            adapterTypeBuilder.addMethod(writeFunctions.createWriteMethod(modelClassName, rootGsonObject, properties.serializeNulls))

        } else {
            // Create an empty method for the write, since we do not support writing for interfaces.
            adapterTypeBuilder.interfaceMethod("write") {
                addParameter(JsonWriter::class.java, "out")
                addParameter(modelClassName, "value")
                addException(IOException::class.java)
            }
        }

        // Adds any required subtype type adapters depending on the usage of the GsonSubtype annotation.
        subtypeFunctions.addSubTypeTypeAdapters(adapterTypeBuilder, rootGsonObject)

        if (adapterTypeBuilder.writeFile(fileWriter, logger, adapterClassName.packageName(), this::onJavaFileBuilt)) {
            return HandleResult(modelClassName, adapterClassName)
        }

        throw ProcessingException("Failed to write generated file: " + adapterClassName.simpleName())
    }

    private fun onJavaFileBuilt(builder: JavaFile.Builder) {
        builder.addStaticImport(GsonUtil::class.java, "*")
    }

    /**
     * Finds a constructor within the input [TypeElement] that has at least one argument.
     *
     * @param modelElement the model being searched.
     */
    private fun findNonEmptyConstructor(modelElement: TypeElement): ExecutableType? {
        return typeHandler.getAllMembers(modelElement)
                .filter { it.kind == ElementKind.CONSTRUCTOR }
                .map { (it.asType() as ExecutableType) }
                .find { it.parameterTypes.size > 0 }
    }
}
