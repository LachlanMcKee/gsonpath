package gsonpath.adapter.enums

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import gsonpath.AutoGsonAdapter
import gsonpath.GsonUtil
import gsonpath.ProcessingException
import gsonpath.adapter.AdapterGenerationResult
import gsonpath.adapter.AdapterMethodBuilder
import gsonpath.adapter.Constants
import gsonpath.adapter.standard.adapter.properties.AutoGsonAdapterProperties
import gsonpath.adapter.standard.adapter.properties.AutoGsonAdapterPropertiesFactory
import gsonpath.adapter.util.writeFile
import gsonpath.compiler.CLASS_NAME_STRING
import gsonpath.compiler.generateClassName
import gsonpath.util.*
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class EnumGsonAdapterGenerator(
        private val typeHandler: TypeHandler,
        private val fileWriter: FileWriter,
        private val annotationFetcher: AnnotationFetcher,
        private val enumFieldLabelMapper: EnumFieldLabelMapper
) {

    @Throws(ProcessingException::class)
    fun handle(
            modelElement: TypeElement,
            autoGsonAnnotation: AutoGsonAdapter): AdapterGenerationResult {

        val properties = AutoGsonAdapterPropertiesFactory().create(modelElement, autoGsonAnnotation, false)
        val fields = typeHandler.getFields(modelElement) { it.kind == ElementKind.ENUM_CONSTANT }

        val typeName = ClassName.get(modelElement)
        val adapterClassName = ClassName.get(typeName.packageName(),
                generateClassName(typeName, "GsonTypeAdapter"))

        createEnumAdapterSpec(adapterClassName, modelElement, properties, fields)
                .writeFile(fileWriter, adapterClassName.packageName()) {
                    it.addStaticImport(GsonUtil::class.java, "*")
                }
        return AdapterGenerationResult(arrayOf(typeName), adapterClassName)
    }

    private fun createEnumAdapterSpec(
            adapterClassName: ClassName,
            element: TypeElement,
            properties: AutoGsonAdapterProperties,
            fields: List<FieldElementContent>) = TypeSpecExt.finalClassBuilder(adapterClassName).apply {

        val typeName = ClassName.get(element)
        superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), typeName))
        addAnnotation(Constants.GENERATED_ANNOTATION)

        field("mGson", Gson::class.java) {
            addModifiers(Modifier.PRIVATE, Modifier.FINAL)
        }
        field("nameToConstant", TypeNameExt.createMap(CLASS_NAME_STRING, typeName)) {
            addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            initializer("new \$T()", TypeNameExt.createHashMap(CLASS_NAME_STRING, typeName))
        }
        field("constantToName", TypeNameExt.createMap(typeName, CLASS_NAME_STRING)) {
            addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            initializer("new \$T()", TypeNameExt.createHashMap(typeName, CLASS_NAME_STRING))
        }

        // Add the constructor which takes a gson instance for future use.
        constructor {
            addModifiers(Modifier.PUBLIC)
            addParameter(Gson::class.java, "gson")
            code {
                assign("this.mGson", "gson")
                newLine()

                handleFields(element, fields, properties) { enumConstantName, label ->
                    addStatement("nameToConstant.put(\"$label\", \$T.$enumConstantName)", typeName)
                }

                newLine()

                handleFields(element, fields, properties) { enumConstantName, label ->
                    addStatement("constantToName.put(\$T.$enumConstantName, \"$label\")", typeName)
                }
            }
        }

        addMethod(createReadMethod(typeName))
        addMethod(createWriteMethod(typeName))
    }

    private fun createReadMethod(enumTypeName: TypeName) = AdapterMethodBuilder.createReadMethodBuilder(enumTypeName).applyAndBuild {
        code {
            `if`("!isValidValue(${Constants.IN})") {
                `return`(Constants.NULL)
            }
            `return`("nameToConstant.get(in.nextString())")
        }
    }

    private fun createWriteMethod(enumTypeName: TypeName) = AdapterMethodBuilder.createWriteMethodBuilder(enumTypeName).applyAndBuild {
        addStatement("out.value(value == null ? null : constantToName.get(value))")
    }

    private fun handleFields(
            element: TypeElement,
            fields: List<FieldElementContent>,
            properties: AutoGsonAdapterProperties,
            fieldFunc: (String, String) -> Unit) {

        fields.forEach { field ->
            val serializedName = annotationFetcher.getAnnotation(element, field.element, SerializedName::class.java)
            val enumConstantName = field.element.simpleName.toString()
            val label = serializedName?.value
                    ?: enumFieldLabelMapper.map(enumConstantName, properties.gsonFieldNamingPolicy)
            fieldFunc(enumConstantName, label)
        }
    }
}