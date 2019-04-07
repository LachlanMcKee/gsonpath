package gsonpath.dependencies

import gsonpath.adapter.common.SubTypeMetadataFactory
import gsonpath.adapter.standard.adapter.StandardGsonAdapterGenerator
import gsonpath.adapter.standard.factory.TypeAdapterFactoryGenerator
import gsonpath.adapter.enums.EnumGsonAdapterGenerator
import gsonpath.util.FileWriter

data class Dependencies(
        val standardGsonAdapterGenerator: StandardGsonAdapterGenerator,
        val fileWriter: FileWriter,
        val typeAdapterFactoryGenerator: TypeAdapterFactoryGenerator,
        val subTypeMetadataFactory: SubTypeMetadataFactory,
        val enumGsonAdapterGenerator: EnumGsonAdapterGenerator)