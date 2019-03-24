package gsonpath

import gsonpath.generator.adapter.AutoGsonAdapterGenerator
import gsonpath.generator.extension.subtype.SubTypeMetadataFactory
import gsonpath.generator.factory.TypeAdapterFactoryGenerator
import gsonpath.util.FileWriter
import gsonpath.util.TypeHandler

data class Dependencies(
        val autoGsonAdapterGenerator: AutoGsonAdapterGenerator,
        val fileWriter: FileWriter,
        val typeAdapterFactoryGenerator: TypeAdapterFactoryGenerator,
        val subTypeMetadataFactory: SubTypeMetadataFactory,
        val enumAdapterFactory: EnumAdapterFactory)