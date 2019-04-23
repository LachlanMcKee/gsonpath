package gsonpath.adapter.standard.adapter.read

import com.squareup.javapoet.ClassName
import gsonpath.adapter.standard.model.GsonArray
import gsonpath.adapter.standard.model.GsonField
import gsonpath.adapter.standard.model.GsonObject

data class ReadParams(
        val baseElement: ClassName,
        val concreteElement: ClassName,
        val requiresConstructorInjection: Boolean,
        val mandatoryFields: List<GsonField>,
        val rootElements: GsonObject,
        val flattenedFields: List<GsonField>,
        val objectIndexes: List<GsonObject>,
        val arrayIndexes: List<GsonArray>
)