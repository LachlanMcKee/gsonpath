package gsonpath.adapter.enums

import com.google.gson.Gson
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import gsonpath.*
import gsonpath.adapter.AdapterGenerationResult
import gsonpath.adapter.AdapterMethodBuilder
import gsonpath.adapter.Constants
import gsonpath.adapter.Constants.GSON
import gsonpath.adapter.Constants.LISTENER
import gsonpath.adapter.standard.adapter.properties.PropertyFetcher
import gsonpath.adapter.util.writeFile
import gsonpath.compiler.generateClassName
import gsonpath.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class EnumGsonAdapterGenerator(
        private val fileWriter: FileWriter,
        private val enumAdapterPropertiesFactory: EnumAdapterPropertiesFactory
) {

    @Throws(ProcessingException::class)
    fun handle(
            enumElement: TypeElement,
            autoGsonAnnotation: EnumGsonAdapter,
            lazyFactoryMetadata: LazyFactoryMetadata): AdapterGenerationResult {

        val propertyFetcher = PropertyFetcher(enumElement)

        val fieldNamingPolicy = propertyFetcher.getProperty("fieldNamingPolicy",
                autoGsonAnnotation.fieldNamingPolicy,
                lazyFactoryMetadata.annotation.fieldNamingPolicy)

        val properties = enumAdapterPropertiesFactory.create(enumElement, fieldNamingPolicy)
        val typeName = properties.enumTypeName
        val adapterClassName = ClassName.get(typeName.packageName(), generateClassName(typeName, "GsonTypeAdapter"))

        createEnumAdapterSpec(adapterClassName, properties)
                .writeFile(fileWriter, adapterClassName.packageName()) {
                    it.addStaticImport(GsonUtil::class.java, "*")
                }
        return AdapterGenerationResult(arrayOf(typeName), adapterClassName)
    }

    private fun createEnumAdapterSpec(
            adapterClassName: ClassName,
            properties: EnumAdapterProperties) = TypeSpecExt.finalClassBuilder(adapterClassName).apply {

        superclass(ParameterizedTypeName.get(ClassName.get(GsonPathTypeAdapter::class.java), properties.enumTypeName))
        addAnnotation(Constants.GENERATED_ANNOTATION)

        constructor {
            addModifiers(Modifier.PUBLIC)
            addParameter(Gson::class.java, GSON)
            addParameter(GsonPathListener::class.java, LISTENER)
            addStatement("super($GSON, $LISTENER)")
        }

        addMethod(createReadMethod(properties))
        addMethod(createWriteMethod(properties))
    }

    private fun createReadMethod(properties: EnumAdapterProperties): MethodSpec {
        val enumTypeName = properties.enumTypeName
        return AdapterMethodBuilder.createReadMethodBuilder(properties.enumTypeName).applyAndBuild {
            code {
                createVariable(String::class.java, "enumValue", "in.nextString()")
                switch("enumValue") {
                    properties.fields.forEach { (enumValueTypeName, label) ->
                        case("\"$label\"", addBreak = false) {
                            `return`("\$T", enumValueTypeName)
                        }
                    }
                    default(addBreak = false) {
                        if (properties.defaultValue != null) {
                            `if`("listener != null") {
                                addStatement("listener.onDefaultEnum(\$T.class, enumValue)", properties.enumTypeName)
                            }
                            `return`("\$T", properties.defaultValue.enumValueTypeName)
                        } else {
                            addEscapedStatement("""throw new gsonpath.JsonUnexpectedEnumValueException(enumValue, "$enumTypeName")""")
                        }
                    }
                }
            }
        }
    }

    private fun createWriteMethod(properties: EnumAdapterProperties): MethodSpec {
        return AdapterMethodBuilder.createWriteMethodBuilder(properties.enumTypeName).applyAndBuild {
            code {
                switch("value") {
                    properties.fields.forEach { (enumValueTypeName, label) ->
                        case(enumValueTypeName.simpleName()) {
                            addStatement("out.value(\"$label\")")
                        }
                    }
                }
            }
        }
    }
}