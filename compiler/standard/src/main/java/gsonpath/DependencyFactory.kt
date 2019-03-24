package gsonpath

import gsonpath.generator.adapter.AdapterModelMetadataFactory
import gsonpath.generator.adapter.AutoGsonAdapterGenerator
import gsonpath.generator.adapter.read.ReadFunctions
import gsonpath.generator.adapter.write.WriteFunctions
import gsonpath.generator.enums.EnumGsonAdapterGenerator
import gsonpath.generator.extension.subtype.SubTypeMetadataFactoryImpl
import gsonpath.generator.factory.TypeAdapterFactoryGenerator
import gsonpath.generator.interf.InterfaceModelMetadataFactory
import gsonpath.generator.interf.ModelInterfaceGenerator
import gsonpath.model.*
import gsonpath.util.*
import javax.annotation.processing.ProcessingEnvironment

object DependencyFactory {

    fun create(processingEnv: ProcessingEnvironment): Dependencies {

        val fileWriter = FileWriter(processingEnv)
        val defaultValueDetector = DefaultValueDetectorImpl(processingEnv)

        val typeHandler = ProcessorTypeHandler(processingEnv)
        val fieldGetterFinder = FieldGetterFinder(typeHandler)
        val annotationFetcher = AnnotationFetcher(typeHandler, fieldGetterFinder)
        val fieldNamingPolicyMapper = FieldNamingPolicyMapper()
        val gsonObjectFactory = GsonObjectFactory(
                GsonObjectValidator(),
                FieldPathFetcher(SerializedNameFetcher, fieldNamingPolicyMapper))
        val gsonObjectTreeFactory = GsonObjectTreeFactory(gsonObjectFactory)

        val extensions = ExtensionsLoader.loadExtensions(typeHandler, Logger(processingEnv))
        val extensionsHandler = ExtensionsHandler(processingEnv, extensions)
        val readFunctions = ReadFunctions(extensionsHandler)
        val writeFunctions = WriteFunctions(extensionsHandler)
        val modelInterfaceGenerator = ModelInterfaceGenerator(InterfaceModelMetadataFactory(typeHandler), fileWriter)
        val adapterModelMetadataFactory = AdapterModelMetadataFactory(
                FieldInfoFactory(
                        typeHandler,
                        FieldTypeFactory(typeHandler),
                        fieldGetterFinder,
                        annotationFetcher,
                        defaultValueDetector),
                gsonObjectTreeFactory,
                typeHandler,
                modelInterfaceGenerator
        )

        // Handle the standard type adapters.
        return Dependencies(
                autoGsonAdapterGenerator = AutoGsonAdapterGenerator(
                        adapterModelMetadataFactory,
                        fileWriter,
                        readFunctions,
                        writeFunctions),
                fileWriter = fileWriter,
                typeAdapterFactoryGenerator = TypeAdapterFactoryGenerator(
                        fileWriter),
                subTypeMetadataFactory = SubTypeMetadataFactoryImpl(
                        typeHandler),
                enumGsonAdapterGenerator = EnumGsonAdapterGenerator(
                        typeHandler,
                        fileWriter,
                        annotationFetcher,
                        fieldNamingPolicyMapper)
        )
    }
}