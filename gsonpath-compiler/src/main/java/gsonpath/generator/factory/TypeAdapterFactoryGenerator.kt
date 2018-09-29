package gsonpath.generator.factory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec
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

        field("mPackagePrivateLoaders", ArrayTypeName.of(TypeAdapterFactory::class.java)) {
            addModifiers(Modifier.PRIVATE, Modifier.FINAL)
        }

        constructor {
            addModifiers(Modifier.PUBLIC)
            code {
                assignNew("mPackagePrivateLoaders", "\$T[${packageLocalHandleResults.size}]", TypeAdapterFactory::class.java)

                // Add the package local type adapter loaders to the hash map.
                for ((index, packageName) in packageLocalHandleResults.keys.withIndex()) {
                    assignNew("mPackagePrivateLoaders[$index]", "$packageName.$PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME()")
                }
            }
        }

        //
        // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
        //
        overrideMethod("create") {
            returns(TypeAdapter::class.java)
            addParameter(Gson::class.java, "gson")
            addParameter(TypeToken::class.java, "type")

            code {
                `for`("int i = 0; i < mPackagePrivateLoaders.length; i++") {
                    createVariable("TypeAdapter", "typeAdapter", "mPackagePrivateLoaders[i].create(gson, type)")
                    newLine()

                    `if`("typeAdapter != null") {
                        `return`("typeAdapter")
                    }
                }
                `return`("null")
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
            addParameter(Gson::class.java, "gson")
            addParameter(TypeToken::class.java, "type")
            code {
                createVariable("Class", "rawType", "type.getRawType()")

                for ((currentAdapterIndex, result) in packageLocalGsonAdapters.withIndex()) {
                    if (currentAdapterIndex == 0) {
                        ifWithoutClose("rawType.equals(\$T.class)", result.originalClassName) {
                            `return`("new \$T(gson)", result.generatedClassName)
                        }
                    } else {
                        newLine() // New line for easier readability.
                        elseIf("rawType.equals(\$T.class)", result.originalClassName) {
                            `return`("new \$T(gson)", result.generatedClassName)
                        }
                    }
                }

                endControlFlow()
                newLine()
                `return`("null")
            }
        }
        return this
    }

    private companion object {
        private const val PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME = "PackagePrivateTypeAdapterLoader"
    }

}
