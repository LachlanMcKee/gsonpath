package gsonpath.generator

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.TypeName
import gsonpath.util.MethodSpecExt
import java.io.IOException

object AdapterMethodBuilder {
    fun createReadMethodBuilder(returnTypeName: TypeName) = MethodSpecExt.overrideMethodBuilder("read").apply {
        returns(returnTypeName)
        addParameter(JsonReader::class.java, Constants.IN)
        addException(IOException::class.java)
    }

    fun createWriteMethodBuilder(writtenValueTypeName: TypeName) = MethodSpecExt.overrideMethodBuilder("write").apply {
        addParameter(JsonWriter::class.java, Constants.OUT)
        addParameter(writtenValueTypeName, Constants.VALUE)
        addException(IOException::class.java)
    }
}