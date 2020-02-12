package gsonpath.adapter

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.TypeName
import gsonpath.GsonErrors
import gsonpath.util.MethodSpecExt
import java.io.IOException

object AdapterMethodBuilder {
    fun createReadMethodBuilder(returnTypeName: TypeName) = MethodSpecExt.overrideMethodBuilder("readImpl").apply {
        returns(returnTypeName)
        addParameter(JsonReader::class.java, Constants.IN)
        addParameter(GsonErrors::class.java, Constants.GSON_ERRORS)
        addException(IOException::class.java)
    }

    fun createWriteMethodBuilder(writtenValueTypeName: TypeName) = MethodSpecExt.overrideMethodBuilder("writeImpl").apply {
        addParameter(JsonWriter::class.java, Constants.OUT)
        addParameter(writtenValueTypeName, Constants.VALUE)
        addException(IOException::class.java)
    }
}