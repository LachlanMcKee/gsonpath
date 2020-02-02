package gsonpath.adapter.enums

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import gsonpath.*
import gsonpath.adapter.AdapterGenerationResult
import gsonpath.adapter.AdapterMethodBuilder
import gsonpath.adapter.Constants
import gsonpath.adapter.standard.adapter.properties.PropertyFetcher
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
            autoGsonAnnotation: EnumGsonAdapter,
            lazyFactoryMetadata: LazyFactoryMetadata): AdapterGenerationResult {

        val propertyFetcher = PropertyFetcher(modelElement)
        val fieldNamingPolicy = propertyFetcher.getProperty("fieldNamingPolicy",
                autoGsonAnnotation.fieldNamingPolicy,
                lazyFactoryMetadata.annotation.fieldNamingPolicy)

        val fields = typeHandler.getFields(modelElement) { it.kind == ElementKind.ENUM_CONSTANT }

        val typeName = ClassName.get(modelElement)
        val adapterClassName = ClassName.get(typeName.packageName(),
                generateClassName(typeName, "GsonTypeAdapter"))

        createEnumAdapterSpec(adapterClassName, modelElement, fieldNamingPolicy, fields)
                .writeFile(fileWriter, adapterClassName.packageName()) {
                    it.addStaticImport(GsonUtil::class.java, "*")
                }
        return AdapterGenerationResult(arrayOf(typeName), adapterClassName)
    }

    private fun createEnumAdapterSpec(
            adapterClassName: ClassName,
            element: TypeElement,
            fieldNamingPolicy: FieldNamingPolicy,
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

        addMethod(createReadMethod(element, fieldNamingPolicy, fields))
        addMethod(createWriteMethod(element, fieldNamingPolicy, fields))
    }

    private fun createReadMethod(
            element: TypeElement,
            fieldNamingPolicy: FieldNamingPolicy,
            fields: List<FieldElementContent>): MethodSpec {

        val defaultField: FieldElementContent? = fields
                .filter { annotationFetcher.getAnnotation(element, it.element, EnumGsonAdapter.DefaultValue::class.java) != null }
                .apply { if (size > 1) throw ProcessingException("Only one DefaultValue can be defined") }
                .firstOrNull()

        val typeName = ClassName.get(element)
        return AdapterMethodBuilder.createReadMethodBuilder(typeName).applyAndBuild {
            code {
                createVariable(String::class.java, "enumValue", "in.nextString()")
                switch("enumValue") {
                    fields.forEach { field ->
                        handleField(element, field, fieldNamingPolicy) { enumConstantName, label ->
                            case("\"$label\"", addBreak = false) {
                                `return`("$typeName.$enumConstantName")
                            }
                        }
                    }
                    default(addBreak = false) {
                        if (defaultField != null) {
                            handleField(element, defaultField, fieldNamingPolicy) { enumConstantName, _ ->
                                `return`("$typeName.$enumConstantName")
                            }
                        } else {
                            addEscapedStatement("""throw new gsonpath.JsonUnexpectedEnumValueException(enumValue, "$typeName")""")
                        }
                    }
                }
            }
        }
    }

    private fun createWriteMethod(
            element: TypeElement,
            fieldNamingPolicy: FieldNamingPolicy,
            fields: List<FieldElementContent>): MethodSpec {

        val typeName = ClassName.get(element)
        return AdapterMethodBuilder.createWriteMethodBuilder(typeName).applyAndBuild {
            code {
                switch("value") {
                    fields.forEach { field ->
                        handleField(element, field, fieldNamingPolicy) { enumConstantName, label ->
                            case(enumConstantName) {
                                addStatement("out.value(\"$label\")")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleField(
            element: TypeElement,
            field: FieldElementContent,
            fieldNamingPolicy: FieldNamingPolicy,
            fieldFunc: (String, String) -> Unit) {

        val serializedName = annotationFetcher.getAnnotation(element, field.element, SerializedName::class.java)
        val enumConstantName = field.element.simpleName.toString()
        val label = serializedName?.value
                ?: enumFieldLabelMapper.map(enumConstantName, fieldNamingPolicy)
        fieldFunc(enumConstantName, label)
    }
}