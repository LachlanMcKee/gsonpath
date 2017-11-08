package gsonpath.generator.factory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.squareup.javapoet.*
import gsonpath.ProcessingException
import gsonpath.compiler.addNewLine

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier

import gsonpath.generator.Generator
import gsonpath.generator.HandleResult
import javax.lang.model.element.TypeElement

class TypeAdapterFactoryGenerator(processingEnv: ProcessingEnvironment) : Generator(processingEnv) {

    fun generate(factoryElement: TypeElement, generatedGsonAdapters: List<HandleResult>): Boolean {
        if (generatedGsonAdapters.isEmpty()) {
            return false
        }

        // Only interfaces are accepted (for simplicity)
        if (!factoryElement.kind.isInterface) {
            throw ProcessingException("Types annotated with @AutoGsonAdapterFactory must be an interface " +
                    "that directly extends com.google.gson.TypeAdapterFactory.", factoryElement)
        }

        // Ensure that the factory element only extends TypeAdapterFactory
        val factoryInterfaces = factoryElement.interfaces
        if (factoryInterfaces.size != 1 || TypeName.get(factoryInterfaces[0]) != TypeName.get(TypeAdapterFactory::class.java)) {
            throw ProcessingException("Interfaces annotated with @AutoGsonAdapterFactory must extend " +
                    "com.google.gson.TypeAdapterFactory and no other interfaces.", factoryElement)
        }

        val packageLocalHandleResults: Map<String, List<HandleResult>> =
                generatedGsonAdapters.fold(emptyMap()) { map, generatedGsonAdapter ->
                    val packageName = generatedGsonAdapter.adapterClassName.packageName()

                    val newList: List<HandleResult> =
                            map[packageName]?.plus(generatedGsonAdapter) ?: listOf(generatedGsonAdapter)

                    return@fold map.plus(Pair(packageName, newList))
                }

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
    private fun createGsonTypeFactoryImpl(factoryElement: TypeElement, packageLocalHandleResults: Map<String, List<HandleResult>>): Boolean {
        val factoryClassName = ClassName.get(factoryElement)

        val typeBuilder = TypeSpec.classBuilder(factoryClassName.simpleName() + "Impl")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(factoryClassName)

        typeBuilder.addField(FieldSpec.builder(ArrayTypeName.of(TypeAdapterFactory::class.java), "mPackagePrivateLoaders")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build())

        val constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)

        val constructorCodeBlock = CodeBlock.builder()
        constructorCodeBlock.addStatement("mPackagePrivateLoaders = new \$T[${packageLocalHandleResults.size}]", TypeAdapterFactory::class.java)

        // Add the package local type adapter loaders to the hash map.
        for ((index, packageName) in packageLocalHandleResults.keys.withIndex()) {
            constructorCodeBlock.addStatement("mPackagePrivateLoaders[$index] = new $packageName.$PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME()")
        }

        constructorBuilder.addCode(constructorCodeBlock.build())
        typeBuilder.addMethod(constructorBuilder.build())

        //
        // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
        //
        val createMethod = MethodSpec.methodBuilder("create")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeAdapter::class.java)
                .addParameter(Gson::class.java, "gson")
                .addParameter(TypeToken::class.java, "type")

        val codeBlock = CodeBlock.builder()
                .beginControlFlow("for (int i = 0; i < mPackagePrivateLoaders.length; i++)")
                .addStatement("TypeAdapter typeAdapter = mPackagePrivateLoaders[i].create(gson, type)")
                .addNewLine()

                .beginControlFlow("if (typeAdapter != null)")
                .addStatement("return typeAdapter")
                .endControlFlow()

                .endControlFlow()
                .addStatement("return null")

        createMethod.addCode(codeBlock.build())
        typeBuilder.addMethod(createMethod.build())

        return writeFile(factoryClassName.packageName(), typeBuilder)
    }

    private fun createPackageLocalTypeAdapterLoaders(packageName: String, packageLocalGsonAdapters: List<HandleResult>): Boolean {
        val typeBuilder = TypeSpec.classBuilder(ClassName.get(packageName, PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(TypeAdapterFactory::class.java)

        //
        // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
        //
        val createMethod = MethodSpec.methodBuilder("create")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeAdapter::class.java)
                .addParameter(Gson::class.java, "gson")
                .addParameter(TypeToken::class.java, "type")

        val codeBlock = CodeBlock.builder()
                .addStatement("Class rawType = type.getRawType()")

        for ((currentAdapterIndex, result) in packageLocalGsonAdapters.withIndex()) {
            val condition = result.adapterGenericTypeClassNames
                    .map { "rawType.equals(\$T.class)" }
                    .joinToString(separator = " || ")

            if (currentAdapterIndex == 0) {
                codeBlock.beginControlFlow("if ($condition)", *result.adapterGenericTypeClassNames)
            } else {
                codeBlock.addNewLine() // New line for easier readability.
                        .nextControlFlow("else if ($condition)", *result.adapterGenericTypeClassNames)
            }
            codeBlock.addStatement("return new \$T(gson)", result.adapterClassName)
        }

        codeBlock.endControlFlow()
                .addNewLine()
                .addStatement("return null")

        createMethod.addCode(codeBlock.build())
        typeBuilder.addMethod(createMethod.build())

        return writeFile(packageName, typeBuilder)
    }

    companion object {
        private val PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME = "PackagePrivateTypeAdapterLoader"
    }

}
