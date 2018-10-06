package gsonpath.generator.adapter

import com.squareup.javapoet.ClassName
import gsonpath.generator.adapter.read.ReadParams
import gsonpath.model.GsonField
import gsonpath.model.GsonObject
import gsonpath.model.MandatoryFieldInfoFactory

data class AdapterModelMetadata(
        val modelClassName: ClassName,
        val adapterClassName: ClassName,
        val isModelInterface: Boolean,
        val rootGsonObject: GsonObject,
        val serializeNulls: Boolean,
        val mandatoryInfoMap: Map<String, MandatoryFieldInfoFactory.MandatoryFieldInfo>,
        val readParams: ReadParams
)