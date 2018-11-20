package gsonpath.generator.factory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec
import gsonpath.generator.Constants.GSON
import gsonpath.generator.Constants.NULL
import gsonpath.generator.HandleResult
import gsonpath.generator.writeFile
import gsonpath.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class TypeAdapterFactoryGenerator(
        private val fileWriter: FileWriter,
        private val logger: Logger) {

    fun generate(factoryElement: TypeElement, generatedGsonAdapters: List<HandleResult>): Boolean {
        val packageLocalHandleResults = TypeAdapterFactoryHandlersFactory
                .createResults(factoryElement, generatedGsonAdapters)

        for ((packageName, list) in packageLocalHandleResults) {
            if (!createPackageLocalTypeAdapterLoaders(packageName, list)) {
                // If any of the package local adapters fail to generate, we must fail the entire process.
                return false
            }
        }

        return createGsonTypeFactoryImpl(factoryElement, packageLocalHandleResults)
    }

    /**
     * Create the GsonPathLoader which is used by the GsonPathTypeAdapterFactory class.
     */
    private fun createGsonTypeFactoryImpl(
            factoryElement: TypeElement,
            packageLocalHandleResults: Map<String, List<HandleResult>>): Boolean {

        val factoryClassName = ClassName.get(factoryElement)

        return TypeSpecExt.finalClassBuilder(factoryClassName.simpleName() + "Impl")
                .addSuperinterface(factoryClassName)
                .gsonTypeFactoryImplContent(packageLocalHandleResults)
                .writeFile(fileWriter, logger, factoryClassName.packageName())
    }

    private fun TypeSpec.Builder.gsonTypeFactoryImplContent(
            packageLocalHandleResults: Map<String, List<HandleResult>>): TypeSpec.Builder {

        field(PACKAGE_PRIVATE_LOADERS, ArrayTypeName.of(TypeAdapterFactory::class.java)) {
            addModifiers(Modifier.PRIVATE, Modifier.FINAL)
        }

        constructor {
            addModifiers(Modifier.PUBLIC)
            code {
                assignNew(PACKAGE_PRIVATE_LOADERS,
                        "\$T[${packageLocalHandleResults.size}]",
                        TypeAdapterFactory::class.java)

                // Add the package local type adapter loaders to the hash map.
                for ((index, packageName) in packageLocalHandleResults.keys.withIndex()) {
                    assignNew("$PACKAGE_PRIVATE_LOADERS[$index]",
                            "$packageName.$PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME()")
                }
            }
        }

        //
        // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
        //
        overrideMethod("create") {
            returns(TypeAdapter::class.java)
            addParameter(Gson::class.java, GSON)
            addParameter(TypeToken::class.java, "type")

            code {
                `for`("int i = 0; i < $PACKAGE_PRIVATE_LOADERS.length; i++") {
                    createVariable("TypeAdapter", TYPE_ADAPTER, "$PACKAGE_PRIVATE_LOADERS[i].create($GSON, type)")
                    newLine()

                    `if`("$TYPE_ADAPTER != $NULL") {
                        `return`(TYPE_ADAPTER)
                    }
                }
                `return`(NULL)
            }
        }

        return this
    }

    private fun createPackageLocalTypeAdapterLoaders(
            packageName: String,
            packageLocalGsonAdapters: List<HandleResult>): Boolean {

        return TypeSpecExt.finalClassBuilder(ClassName.get(packageName, PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME))
                .addSuperinterface(TypeAdapterFactory::class.java)
                .packagePrivateTypeAdapterLoaderContent(packageLocalGsonAdapters)
                .writeFile(fileWriter, logger, packageName)
    }

    //
    // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
    //
    private fun TypeSpec.Builder.packagePrivateTypeAdapterLoaderContent(
            packageLocalGsonAdapters: List<HandleResult>): TypeSpec.Builder {

        overrideMethod("create") {
            returns(TypeAdapter::class.java)
            addParameter(Gson::class.java, GSON)
            addParameter(TypeToken::class.java, "type")
            code {
                createVariable("Class", RAW_TYPE, "type.getRawType()")

                for ((currentAdapterIndex, result) in packageLocalGsonAdapters.withIndex()) {
                    val condition = result.adapterGenericTypeClassNames
                            .joinToString(separator = " || ") { "rawType.equals(\$T.class)" }

                    if (currentAdapterIndex == 0) {
                        ifWithoutClose(condition, *result.adapterGenericTypeClassNames) {
                            `return`("new \$T($GSON)", result.adapterClassName)
                        }
                    } else {
                        newLine() // New line for easier readability.
                        elseIf(condition, *result.adapterGenericTypeClassNames) {
                            `return`("new \$T($GSON)", result.adapterClassName)
                        }
                    }
                }

                endControlFlow()
                newLine()
                `return`(NULL)
            }
        }
        return this
    }

    private companion object {
        private const val PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME = "PackagePrivateTypeAdapterLoader"
        private const val PACKAGE_PRIVATE_LOADERS = "mPackagePrivateLoaders"
        private const val TYPE_ADAPTER = "typeAdapter"
        private const val RAW_TYPE = "rawType"
    }
}
