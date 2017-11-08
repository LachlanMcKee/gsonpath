package gsonpath.generator.standard

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.squareup.javapoet.*
import gsonpath.*
import gsonpath.compiler.GsonPathExtension
import gsonpath.ProcessingException
import gsonpath.compiler.generateClassName
import gsonpath.generator.Generator
import gsonpath.generator.HandleResult
import gsonpath.generator.interf.ModelInterfaceGenerator
import gsonpath.model.*
import javax.annotation.Generated
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

class AutoGsonAdapterGenerator(processingEnv: ProcessingEnvironment) : Generator(processingEnv) {

    @Throws(ProcessingException::class)
    fun handle(modelElement: TypeElement, extensions: List<GsonPathExtension>): HandleResult {
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

        val autoGsonAnnotation = modelElement.getAnnotation(AutoGsonAdapter::class.java)

        val concreteClassName: ClassName
        val fieldInfoList: List<FieldInfo>
        val isModelInterface = modelElement.kind.isInterface

        val properties = AutoGsonAdapterPropertiesFactory().create(
                autoGsonAnnotation, getDefaultsAnnotation(autoGsonAnnotation), isModelInterface)

        val requiresConstructorInjection: Boolean =
                if (isModelInterface) {
                    true
                } else {
                    findNonEmptyConstructor(processingEnv, modelElement) != null
                }

        val fieldInfoFactory = FieldInfoFactory(processingEnv)
        val adapterGenericTypeClassNames: Array<ClassName>

        if (!isModelInterface) {
            concreteClassName = modelClassName

            fieldInfoList = fieldInfoFactory.getModelFieldsFromElement(
                    modelElement,
                    properties.fieldsRequireAnnotation,
                    requiresConstructorInjection)

            adapterGenericTypeClassNames = arrayOf(modelClassName)

        } else {
            val interfaceInfo = ModelInterfaceGenerator(processingEnv).handle(modelElement)
            concreteClassName = interfaceInfo.parentClassName

            fieldInfoList = fieldInfoFactory.getModelFieldsFromInterface(interfaceInfo)

            // The adapter should handle both the interface and concrete class.
            adapterGenericTypeClassNames = arrayOf(interfaceInfo.parentClassName, modelClassName)
        }

        val rootGsonObject = GsonObjectTreeFactory().createGsonObject(fieldInfoList, properties.rootField,
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

        adapterTypeBuilder.addMethod(createWriteMethod(modelClassName, rootGsonObject, properties.serializeNulls))

        // Adds any required subtype type adapters depending on the usage of the GsonSubtype annotation.
        addSubTypeTypeAdapters(processingEnv, adapterTypeBuilder, rootGsonObject)

        if (writeFile(adapterClassName.packageName(), adapterTypeBuilder)) {
            return HandleResult(adapterGenericTypeClassNames, adapterClassName)
        }

        throw ProcessingException("Failed to write generated file: " + adapterClassName.simpleName())
    }

    public override fun onJavaFileBuilt(builder: JavaFile.Builder) {
        builder.addStaticImport(GsonUtil::class.java, "*")
    }

    @Throws(ProcessingException::class)
    private fun getDefaultsAnnotation(autoGsonAnnotation: AutoGsonAdapter): GsonPathDefaultConfiguration? {
        // Annotation processors seem to make obtaining this value difficult!
        val defaultsTypeMirror: TypeMirror? =
                try {
                    autoGsonAnnotation.defaultConfiguration
                    null
                } catch (mte: MirroredTypeException) {
                    mte.typeMirror
                }

        val defaultsElement = processingEnv.typeUtils.asElement(defaultsTypeMirror)

        if (defaultsElement != null) {
            // If an inheritable annotation is used, used the default instead.
            return defaultsElement.getAnnotation(GsonPathDefaultConfiguration::class.java) ?:
                    throw ProcessingException("Defaults property must point to a class which uses the @GsonPathDefaultConfiguration annotation")
        }

        return null
    }


    /**
     * Finds a constructor within the input [TypeElement] that has at least one argument.
     *
     * @param processingEnv the annotation processor environment.
     * @param modelElement the model being searched.
     */
    fun findNonEmptyConstructor(processingEnv: ProcessingEnvironment, modelElement: TypeElement): ExecutableType? {
        return processingEnv.elementUtils.getAllMembers(modelElement)
                .filter { it.kind == ElementKind.CONSTRUCTOR }
                .map { (it.asType() as ExecutableType) }
                .find { it.parameterTypes.size > 0 }
    }
}
