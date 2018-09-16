package gsonpath.generator.factory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
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

        val typeBuilder = TypeSpecExt.finalClassBuilder(factoryClassName.simpleName() + "Impl")
                .addSuperinterface(factoryClassName)

        typeBuilder.addField(FieldSpec.builder(ArrayTypeName.of(TypeAdapterFactory::class.java), "mPackagePrivateLoaders")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build())

        val constructor = MethodSpec.constructorBuilder().applyAndBuild {
            addModifiers(Modifier.PUBLIC)
            code {
                addStatement("mPackagePrivateLoaders = new \$T[${packageLocalHandleResults.size}]", TypeAdapterFactory::class.java)

                // Add the package local type adapter loaders to the hash map.
                for ((index, packageName) in packageLocalHandleResults.keys.withIndex()) {
                    addStatement("mPackagePrivateLoaders[$index] = new $packageName.$PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME()")
                }
            }
        }

        typeBuilder.addMethod(constructor)

        //
        // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
        //
        val createMethod = MethodSpecExt.interfaceMethodBuilder("create").applyAndBuild {
            returns(TypeAdapter::class.java)
            addParameter(Gson::class.java, "gson")
            addParameter(TypeToken::class.java, "type")

            code {
                `for`("int i = 0; i < mPackagePrivateLoaders.length; i++") {
                    addStatement("TypeAdapter typeAdapter = mPackagePrivateLoaders[i].create(gson, type)")
                    newLine()

                    `if`("typeAdapter != null") {
                        addStatement("return typeAdapter")
                    }
                }
                addStatement("return null")
            }
        }
        typeBuilder.addMethod(createMethod)

        return typeBuilder.writeFile(fileWriter, logger, factoryClassName.packageName())
    }

    private fun createPackageLocalTypeAdapterLoaders(
            packageName: String,
            packageLocalGsonAdapters: List<HandleResult>): Boolean {

        val typeBuilder = TypeSpecExt.finalClassBuilder(ClassName.get(packageName, PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME))
                .addSuperinterface(TypeAdapterFactory::class.java)

        //
        // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
        //
        val createMethod = MethodSpecExt.interfaceMethodBuilder("create").applyAndBuild {
            returns(TypeAdapter::class.java)
            addParameter(Gson::class.java, "gson")
            addParameter(TypeToken::class.java, "type")
            code {
                addStatement("Class rawType = type.getRawType()")

                for ((currentAdapterIndex, result) in packageLocalGsonAdapters.withIndex()) {
                    if (currentAdapterIndex == 0) {
                        beginControlFlow("if (rawType.equals(\$T.class))", result.originalClassName)
                    } else {
                        newLine() // New line for easier readability.
                        nextControlFlow("else if (rawType.equals(\$T.class))", result.originalClassName)
                    }
                    addStatement("return new \$T(gson)", result.generatedClassName)
                }

                endControlFlow()
                newLine()
                addStatement("return null")
            }
        }
        typeBuilder.addMethod(createMethod)

        return typeBuilder.writeFile(fileWriter, logger, packageName)
    }

    private companion object {
        private const val PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME = "PackagePrivateTypeAdapterLoader"
    }

}
