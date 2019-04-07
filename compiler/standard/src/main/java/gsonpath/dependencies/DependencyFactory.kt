package gsonpath.dependencies

import gsonpath.adapter.common.SubTypeMetadataFactory
import gsonpath.adapter.common.SubTypeMetadataFactoryImpl
import gsonpath.adapter.standard.adapter.AdapterModelMetadataFactory
import gsonpath.adapter.standard.adapter.StandardGsonAdapterGenerator
import gsonpath.adapter.standard.adapter.read.ReadFunctions
import gsonpath.adapter.standard.adapter.write.WriteFunctions
import gsonpath.adapter.standard.extension.ExtensionsHandler
import gsonpath.adapter.standard.extension.ExtensionsLoader
import gsonpath.adapter.standard.extension.def.intdef.IntDefExtension
import gsonpath.adapter.standard.extension.def.stringdef.StringDefExtension
import gsonpath.adapter.standard.extension.empty.EmptyToNullExtension
import gsonpath.adapter.standard.extension.flatten.FlattenJsonExtension
import gsonpath.adapter.standard.extension.invalid.RemoveInvalidElementsExtension
import gsonpath.adapter.standard.extension.range.floatrange.FloatRangeExtension
import gsonpath.adapter.standard.extension.range.intrange.IntRangeExtension
import gsonpath.adapter.standard.extension.size.SizeExtension
import gsonpath.adapter.standard.extension.subtype.GsonSubTypeExtension
import gsonpath.adapter.standard.factory.TypeAdapterFactoryGenerator
import gsonpath.adapter.standard.interf.InterfaceModelMetadataFactory
import gsonpath.adapter.standard.interf.ModelInterfaceGenerator
import gsonpath.adapter.standard.model.*
import gsonpath.compiler.GsonPathExtension
import gsonpath.adapter.enums.EnumGsonAdapterGenerator
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

        val subTypeMetadataFactory = SubTypeMetadataFactoryImpl(typeHandler)
        val extensions = loadExtensions(typeHandler, subTypeMetadataFactory, processingEnv)
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
                standardGsonAdapterGenerator = StandardGsonAdapterGenerator(
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

    private fun loadExtensions(
            typeHandler: TypeHandler,
            subTypeMetadataFactory: SubTypeMetadataFactory,
            processingEnv: ProcessingEnvironment): List<GsonPathExtension> {

        return ExtensionsLoader.loadExtensions(Logger(processingEnv))
                .plus(arrayOf(
                        IntDefExtension(),
                        StringDefExtension(),
                        EmptyToNullExtension(),
                        FlattenJsonExtension(),
                        RemoveInvalidElementsExtension(),
                        FloatRangeExtension(),
                        IntRangeExtension(),
                        SizeExtension(),
                        GsonSubTypeExtension(typeHandler, subTypeMetadataFactory)
                ))
    }
}