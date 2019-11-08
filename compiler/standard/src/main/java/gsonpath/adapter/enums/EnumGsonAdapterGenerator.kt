package gsonpath.adapter.enums

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import gsonpath.AutoGsonAdapter
import gsonpath.GsonPathTypeAdapter
import gsonpath.GsonUtil
import gsonpath.ProcessingException
import gsonpath.adapter.AdapterMethodBuilder
import gsonpath.adapter.Constants
import gsonpath.adapter.standard.adapter.properties.AutoGsonAdapterProperties
import gsonpath.adapter.standard.adapter.properties.AutoGsonAdapterPropertiesFactory
import gsonpath.adapter.util.writeFile
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
            autoGsonAnnotation: AutoGsonAdapter) {

        val properties = AutoGsonAdapterPropertiesFactory().create(modelElement, autoGsonAnnotation, false)
        val fields = typeHandler.getFields(modelElement) { it.kind == ElementKind.ENUM_CONSTANT }

        val typeName = ClassName.get(modelElement)
        val adapterClassName = ClassName.get(typeName.packageName(),
                generateClassName(typeName, "GsonTypeAdapter"))

        createEnumAdapterSpec(adapterClassName, modelElement, properties, fields)
                .addOriginatingElement(modelElement)
                .writeFile(fileWriter, adapterClassName.packageName()) {
                    it.addStaticImport(GsonUtil::class.java, "*")
                }
    }

    private fun createEnumAdapterSpec(
            adapterClassName: ClassName,
            element: TypeElement,
            properties: AutoGsonAdapterProperties,
            fields: List<FieldElementContent>) = TypeSpecExt.finalClassBuilder(adapterClassName).apply {

        val typeName = ClassName.get(element)
        superclass(ParameterizedTypeName.get(ClassName.get(GsonPathTypeAdapter::class.java), typeName))
        addAnnotation(Constants.GENERATED_ANNOTATION)

        // Add the constructor which takes a gson instance for future use.
        constructor {
            addModifiers(Modifier.PUBLIC)
            addParameter(Gson::class.java, "gson")
            code {
                addStatement("super(gson)")
            }
        }

        addMethod(createReadMethod(element, properties, fields))
        addMethod(createWriteMethod(element, properties, fields))
    }

    private fun createReadMethod(
            element: TypeElement,
            properties: AutoGsonAdapterProperties,
            fields: List<FieldElementContent>): MethodSpec {

        val typeName = ClassName.get(element)
        return AdapterMethodBuilder.createReadMethodBuilder(typeName).applyAndBuild {
            code {
                switch("in.nextString()") {
                    handleFields(element, fields, properties) { enumConstantName, label ->
                        case("\"$label\"", addBreak = false) {
                            `return`("$typeName.$enumConstantName"
                            )
                        }
                    }
                    default(addBreak = false) {
                        `return`("null")
                    }
                }
            }
        }
    }

    private fun createWriteMethod(
            element: TypeElement,
            properties: AutoGsonAdapterProperties,
            fields: List<FieldElementContent>): MethodSpec {

        val typeName = ClassName.get(element)
        return AdapterMethodBuilder.createWriteMethodBuilder(typeName).applyAndBuild {
            code {
                switch("value") {
                    handleFields(element, fields, properties) { enumConstantName, label ->
                        case(enumConstantName) {
                            addStatement("out.value(\"$label\")")
                        }
                    }
                }
            }
        }
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