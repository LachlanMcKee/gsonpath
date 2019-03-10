package gsonpath

import gsonpath.compiler.GsonPathExtension
import gsonpath.generator.adapter.AdapterModelMetadataFactory
import gsonpath.generator.adapter.AutoGsonAdapterGenerator
import gsonpath.generator.adapter.read.ReadFunctions
import gsonpath.generator.adapter.write.WriteFunctions
import gsonpath.generator.factory.TypeAdapterFactoryGenerator
import gsonpath.generator.interf.InterfaceModelMetadataFactory
import gsonpath.generator.interf.ModelInterfaceGenerator
import gsonpath.model.*
import gsonpath.util.*
import javax.annotation.processing.ProcessingEnvironment

object DependencyFactory {

    fun create(
            processingEnv: ProcessingEnvironment,
            extensions: List<GsonPathExtension>): Dependencies {

        val fileWriter = FileWriter(processingEnv)
        val logger = LoggerImpl(processingEnv)
        val defaultValueDetector = DefaultValueDetectorImpl(processingEnv)

        val typeHandler = ProcessorTypeHandler(processingEnv)
        val fieldGetterFinder = FieldGetterFinder(typeHandler)
        val annotationFetcher = AnnotationFetcher(typeHandler, fieldGetterFinder)
        val gsonObjectFactory = GsonObjectFactory(
                GsonObjectValidator(),
                FieldPathFetcher(SerializedNameFetcher, FieldNamingPolicyMapper()))
        val gsonObjectTreeFactory = GsonObjectTreeFactory(gsonObjectFactory)
        val extensionsHandler = ExtensionsHandler(processingEnv, extensions)
        val readFunctions = ReadFunctions(extensionsHandler)
        val writeFunctions = WriteFunctions(extensionsHandler)
        val modelInterfaceGenerator = ModelInterfaceGenerator(InterfaceModelMetadataFactory(typeHandler), fileWriter, logger)
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
                        writeFunctions,
                        logger),
                typeAdapterFactoryGenerator = TypeAdapterFactoryGenerator(
                        fileWriter,
                        logger)
        )
    }
}