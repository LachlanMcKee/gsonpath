package gsonpath.adapter.standard.adapter

import com.squareup.javapoet.ClassName
import gsonpath.adapter.standard.adapter.read.ReadParams
import gsonpath.adapter.standard.adapter.write.WriteParams
import gsonpath.adapter.standard.model.GsonObject
import gsonpath.adapter.standard.model.MandatoryFieldInfoFactory

data class AdapterModelMetadata(
        val modelClassName: ClassName,
        val adapterGenericTypeClassNames: List<ClassName>,
        val adapterClassName: ClassName,
        val isModelInterface: Boolean,
        val rootGsonObject: GsonObject,
        val mandatoryInfoMap: Map<String, MandatoryFieldInfoFactory.MandatoryFieldInfo>,
        val readParams: ReadParams,
        val writeParams: WriteParams
)