package gsonpath.generator.standard.read

import com.squareup.javapoet.ClassName
import gsonpath.model.GsonField
import gsonpath.model.GsonObject
import gsonpath.model.MandatoryFieldInfoFactory

data class ReadParams(
        val baseElement: ClassName,
        val concreteElement: ClassName,
        val requiresConstructorInjection: Boolean,
        val mandatoryInfoMap: Map<String, MandatoryFieldInfoFactory.MandatoryFieldInfo>,
        val rootElements: GsonObject,
        val flattenedFields: List<GsonField>
)