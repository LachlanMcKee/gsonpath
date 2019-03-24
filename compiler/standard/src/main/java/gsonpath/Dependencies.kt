package gsonpath

import gsonpath.generator.adapter.AutoGsonAdapterGenerator
import gsonpath.generator.enums.EnumGsonAdapterGenerator
import gsonpath.generator.extension.subtype.SubTypeMetadataFactory
import gsonpath.generator.factory.TypeAdapterFactoryGenerator
import gsonpath.util.FileWriter

data class Dependencies(
        val autoGsonAdapterGenerator: AutoGsonAdapterGenerator,
        val fileWriter: FileWriter,
        val typeAdapterFactoryGenerator: TypeAdapterFactoryGenerator,
        val subTypeMetadataFactory: SubTypeMetadataFactory,
        val enumGsonAdapterGenerator: EnumGsonAdapterGenerator)