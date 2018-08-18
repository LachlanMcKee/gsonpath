package gsonpath.generator.standard

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.*
import gsonpath.AutoGsonAdapter
import gsonpath.GsonUtil
import gsonpath.ProcessingException
import gsonpath.compiler.GsonPathExtension
import gsonpath.compiler.generateClassName
import gsonpath.generator.HandleResult
import gsonpath.generator.interf.ModelInterfaceGenerator
import gsonpath.generator.writeFile
import gsonpath.model.FieldInfo
import gsonpath.model.FieldInfoFactory
import gsonpath.model.GsonObjectTreeFactory
import gsonpath.model.MandatoryFieldInfoFactory
import java.io.IOException
import javax.annotation.Generated
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType

class AutoGsonAdapterGenerator(private val processingEnv: ProcessingEnvironment) {

    @Throws(ProcessingException::class)
    fun handle(modelElement: TypeElement,
               autoGsonAnnotation: AutoGsonAdapter,
               extensions: List<GsonPathExtension>): HandleResult {

        val modelClassName = ClassName.get(modelElement)
        val adapterClassName = ClassName.get(modelClassName.packageName(),
                generateClassName(modelClassName, "GsonTypeAdapter"))

        val generatedJavaPoetAnnotation = AnnotationSpec.builder(Generated::class.java)
                .addMember("value", "\"gsonpath.GsonProcessor\"")
                .addMember("comments", "\"https://github.com/LachlanMcKee/gsonpath\"")
                .build()

        val adapterTypeBuilder = TypeSpec.classBuilder(adapterClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), modelClassName))
                .addAnnotation(generatedJavaPoetAnnotation)
                .addField(Gson::class.java, "mGson", Modifier.PRIVATE, Modifier.FINAL)

        // Add the constructor which takes a gson instance for future use.
        adapterTypeBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Gson::class.java, "gson")
                .addStatement("this.\$N = \$N", "mGson", "gson")
                .build())

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

        val fieldInfoFactory = FieldInfoFactory(processingEnv)
        if (!isModelInterface) {
            concreteClassName = modelClassName

            fieldInfoList = fieldInfoFactory.getModelFieldsFromElement(
                    modelElement,
                    properties.fieldsRequireAnnotation,
                    requiresConstructorInjection)

        } else {
            val interfaceInfo = ModelInterfaceGenerator(processingEnv).handle(modelElement)
            concreteClassName = interfaceInfo.parentClassName

            fieldInfoList = fieldInfoFactory.getModelFieldsFromInterface(interfaceInfo)
        }

        val rootGsonObject = GsonObjectTreeFactory(SubTypeMetadataFactory(processingEnv))
                .createGsonObject(fieldInfoList, properties.rootField,
                        properties.flattenDelimiter, properties.gsonFieldNamingPolicy, properties.gsonFieldValidationType,
                        properties.pathSubstitutions)

        // Adds the mandatory field index constants and also populates the mandatoryInfoMap values.
        val mandatoryInfoMap = MandatoryFieldInfoFactory().createMandatoryFieldsFromGsonObject(rootGsonObject)
        if (mandatoryInfoMap.isNotEmpty()) {
            mandatoryInfoMap.values
                    .mapIndexed { mandatoryIndex, mandatoryField ->
                        FieldSpec.builder(TypeName.INT, mandatoryField.indexVariableName)
                                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                                .initializer("" + mandatoryIndex)
                                .build()
                    }
                    .forEach { adapterTypeBuilder.addField(it) }

            adapterTypeBuilder.addField(FieldSpec.builder(TypeName.INT, "MANDATORY_FIELDS_SIZE")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("" + mandatoryInfoMap.size)
                    .build())
        }

        adapterTypeBuilder.addMethod(createReadMethod(processingEnv, modelClassName, concreteClassName,
                requiresConstructorInjection, mandatoryInfoMap, rootGsonObject, extensions))

        if (!isModelInterface) {
            adapterTypeBuilder.addMethod(createWriteMethod(modelClassName, rootGsonObject, properties.serializeNulls))

        } else {
            // Create an empty method for the write, since we do not support writing for interfaces.
            val writeMethod = MethodSpec.methodBuilder("write")
                    .addAnnotation(Override::class.java)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(JsonWriter::class.java, "out")
                    .addParameter(modelClassName, "value")
                    .addException(IOException::class.java)

            adapterTypeBuilder.addMethod(writeMethod.build())
        }

        // Adds any required subtype type adapters depending on the usage of the GsonSubtype annotation.
        addSubTypeTypeAdapters(processingEnv, adapterTypeBuilder, rootGsonObject)

        if (adapterTypeBuilder.writeFile(processingEnv, adapterClassName.packageName(), this::onJavaFileBuilt)) {
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
        return processingEnv.elementUtils.getAllMembers(modelElement)
                .filter { it.kind == ElementKind.CONSTRUCTOR }
                .map { (it.asType() as ExecutableType) }
                .find { it.parameterTypes.size > 0 }
    }
}
