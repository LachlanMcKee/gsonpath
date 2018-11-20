package gsonpath.generator.adapter.write

import com.squareup.javapoet.ClassName
import gsonpath.model.GsonObject

data class WriteParams(
        val elementClassName: ClassName,
        val rootElements: GsonObject,
        val serializeNulls: Boolean
)