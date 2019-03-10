package gsonpath

import gsonpath.generator.adapter.AutoGsonAdapterGenerator
import gsonpath.generator.factory.TypeAdapterFactoryGenerator

data class Dependencies(
        val autoGsonAdapterGenerator: AutoGsonAdapterGenerator,
        val typeAdapterFactoryGenerator: TypeAdapterFactoryGenerator)