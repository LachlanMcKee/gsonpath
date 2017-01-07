package gsonpath.generator.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.squareup.javapoet.*

import java.util.*

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier

import gsonpath.generator.Generator
import gsonpath.generator.HandleResult
import gsonpath.internal.TypeAdapterLoader

class TypeAdapterLoaderGenerator(processingEnv: ProcessingEnvironment) : Generator(processingEnv) {

    fun generate(generatedGsonAdapters: List<HandleResult>): Boolean {
        if (generatedGsonAdapters.isEmpty()) {
            return false
        }

        val packageLocalHandleResults = HashMap<String, MutableList<HandleResult>>()
        for (generatedGsonAdapter in generatedGsonAdapters) {
            val packageName = generatedGsonAdapter.generatedClassName.packageName()

            var localResults = packageLocalHandleResults[packageName]
            if (localResults == null) {
                localResults = ArrayList<HandleResult>()
                packageLocalHandleResults.put(packageName, localResults)
            }

            localResults.add(generatedGsonAdapter)
        }

        for ((packageName, list) in packageLocalHandleResults) {
            if (!createPackageLocalTypeAdapterLoaders(packageName, list)) {
                // If any of the package local adapters fail to generate, we must fail the entire process.
                return false
            }
        }

        return createRootTypeAdapterLoader(packageLocalHandleResults)
    }

    /**
     * Create the GsonPathLoader which is used by the GsonPathTypeAdapterFactory class.
     */
    private fun createRootTypeAdapterLoader(packageLocalHandleResults: Map<String, List<HandleResult>>): Boolean {
        val typeBuilder = TypeSpec.classBuilder("GeneratedTypeAdapterLoader")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(TypeAdapterLoader::class.java)

        typeBuilder.addField(FieldSpec.builder(ArrayTypeName.of(TypeAdapterLoader::class.java), "mPackagePrivateLoaders")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build())

        val constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)

        val constructorCodeBlock = CodeBlock.builder()
        constructorCodeBlock.addStatement("mPackagePrivateLoaders = new \$T[\$L]", TypeAdapterLoader::class.java, packageLocalHandleResults.size)

        // Add the package local type adapter loaders to the hash map.
        for ((index, packageName) in packageLocalHandleResults.keys.withIndex()) {
            constructorCodeBlock.addStatement("mPackagePrivateLoaders[\$L] = new \$L.\$L()", index, packageName, PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME)
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
        codeBlock.beginControlFlow("for (int i = 0; i < mPackagePrivateLoaders.length; i++)")
        codeBlock.addStatement("TypeAdapter typeAdapter = mPackagePrivateLoaders[i].create(gson, type)")
        codeBlock.add("\n")

        codeBlock.beginControlFlow("if (typeAdapter != null)")
        codeBlock.addStatement("return typeAdapter")
        codeBlock.endControlFlow()

        codeBlock.endControlFlow()
        codeBlock.addStatement("return null")

        createMethod.addCode(codeBlock.build())
        typeBuilder.addMethod(createMethod.build())

        return writeFile("gsonpath", typeBuilder)
    }

    private fun createPackageLocalTypeAdapterLoaders(packageName: String, packageLocalGsonAdapters: List<HandleResult>): Boolean {
        val typeBuilder = TypeSpec.classBuilder(ClassName.get(packageName, PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(TypeAdapterLoader::class.java)

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
        codeBlock.addStatement("Class rawType = type.getRawType()")

        for ((currentAdapterIndex, result) in packageLocalGsonAdapters.withIndex()) {
            if (currentAdapterIndex == 0) {
                codeBlock.beginControlFlow("if (rawType.equals(\$T.class))", result.originalClassName)
            } else {
                codeBlock.add("\n") // New line for easier readability.
                codeBlock.nextControlFlow("else if (rawType.equals(\$T.class))", result.originalClassName)
            }
            codeBlock.addStatement("return new \$T(gson)", result.generatedClassName)
        }

        codeBlock.endControlFlow()
        codeBlock.add("\n")
        codeBlock.addStatement("return null")

        createMethod.addCode(codeBlock.build())
        typeBuilder.addMethod(createMethod.build())

        return writeFile(packageName, typeBuilder)
    }

    companion object {
        private val PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME = "PackagePrivateTypeAdapterLoader"
    }

}