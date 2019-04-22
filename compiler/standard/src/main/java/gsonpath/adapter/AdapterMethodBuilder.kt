package gsonpath.adapter

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import gsonpath.util.MethodSpecExt
import gsonpath.util.applyAndBuild
import java.io.IOException

object AdapterMethodBuilder {
    fun createReadMethodBuilder(returnTypeName: TypeName) = MethodSpecExt.overrideMethodBuilder("readImpl").apply {
        returns(returnTypeName)
        addParameter(JsonReader::class.java, Constants.IN)
        addException(IOException::class.java)
    }

    fun createWriteMethodBuilder(writtenValueTypeName: TypeName) = MethodSpecExt.overrideMethodBuilder("writeImpl").apply {
        addParameter(JsonWriter::class.java, Constants.OUT)
        addParameter(writtenValueTypeName, Constants.VALUE)
        addException(IOException::class.java)
    }

    fun createModelClassNameMethod(typeName: TypeName) = MethodSpecExt.overrideMethodBuilder("getModelClassName").applyAndBuild {
        returns(ClassName.get(String::class.java))
        addStatement("""return "$typeName"""")
    }
}