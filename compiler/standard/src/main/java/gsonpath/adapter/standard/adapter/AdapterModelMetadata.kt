package gsonpath.adapter.standard.adapter

import com.squareup.javapoet.ClassName
import gsonpath.adapter.standard.adapter.read.ReadParams
import gsonpath.adapter.standard.adapter.write.WriteParams
import gsonpath.adapter.standard.model.GsonField
import gsonpath.adapter.standard.model.GsonObject

data class AdapterModelMetadata(
        val modelClassName: ClassName,
        val adapterClassName: ClassName,
        val isModelInterface: Boolean,
        val rootGsonObject: GsonObject,
        val mandatoryFields: List<GsonField>,
        val readParams: ReadParams,
        val writeParams: WriteParams
)