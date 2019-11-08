package gsonpath.adapter.standard.factory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec
import gsonpath.adapter.AdapterGenerationResult
import gsonpath.adapter.Constants.GSON
import gsonpath.adapter.Constants.NULL
import gsonpath.adapter.util.writeFile
import gsonpath.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class TypeAdapterFactoryGenerator(private val fileWriter: FileWriter) {

    fun generate(factoryElement: TypeElement, generatedGsonAdapters: List<AdapterGenerationResult>) {
        val packageLocalHandleResults = TypeAdapterFactoryHandlersFactory
                .createResults(factoryElement, generatedGsonAdapters)

        for ((packageName, list) in packageLocalHandleResults) {
            createPackageLocalTypeAdapterLoaders(packageName, list)
        }

        createGsonTypeFactoryImpl(factoryElement, packageLocalHandleResults)
    }

    /**
     * Create the GsonPathLoader which is used by the GsonPathTypeAdapterFactory class.
     */
    private fun createGsonTypeFactoryImpl(
            factoryElement: TypeElement,
            packageLocalAdapterGenerationResults: Map<String, List<AdapterGenerationResult>>) {

        val factoryClassName = ClassName.get(factoryElement)

        TypeSpecExt.finalClassBuilder(factoryClassName.simpleName() + "Impl")
                .addSuperinterface(factoryClassName)
                .gsonTypeFactoryImplContent(packageLocalAdapterGenerationResults)
                TODO - GUESS WE ARE AGGREGATING :(
                .writeFile(fileWriter, factoryClassName.packageName())
    }

    private fun TypeSpec.Builder.gsonTypeFactoryImplContent(
            packageLocalAdapterGenerationResults: Map<String, List<AdapterGenerationResult>>): TypeSpec.Builder {

        field(PACKAGE_PRIVATE_LOADERS, ArrayTypeName.of(TypeAdapterFactory::class.java)) {
            addModifiers(Modifier.PRIVATE, Modifier.FINAL)
        }

        constructor {
            addModifiers(Modifier.PUBLIC)
            code {
                assignNew(PACKAGE_PRIVATE_LOADERS,
                        "\$T[${packageLocalAdapterGenerationResults.size}]",
                        TypeAdapterFactory::class.java)

                // Add the package local type adapter loaders to the hash map.
                for ((index, packageName) in packageLocalAdapterGenerationResults.keys.withIndex()) {
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
                    createVariable(TypeAdapter::class.java, TYPE_ADAPTER, "$PACKAGE_PRIVATE_LOADERS[i].create($GSON, type)")
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
            packageLocalGsonAdapters: List<AdapterGenerationResult>) {

        TypeSpecExt.finalClassBuilder(ClassName.get(packageName, PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME))
                .addSuperinterface(TypeAdapterFactory::class.java)
                .packagePrivateTypeAdapterLoaderContent(packageLocalGsonAdapters)
        TODO - GUESS WE ARE AGGREGATING :(
                .writeFile(fileWriter, packageName)
    }

    //
    // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
    //
    private fun TypeSpec.Builder.packagePrivateTypeAdapterLoaderContent(
            packageLocalGsonAdapters: List<AdapterGenerationResult>): TypeSpec.Builder {

        overrideMethod("create") {
            returns(TypeAdapter::class.java)
            addParameter(Gson::class.java, GSON)
            addParameter(TypeToken::class.java, "type")
            code {
                createVariable(Class::class.java, RAW_TYPE, "type.getRawType()")

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
