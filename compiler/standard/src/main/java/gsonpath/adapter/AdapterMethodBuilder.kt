package gsonpath.adapter

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.TypeName
import gsonpath.util.MethodSpecExt
import java.io.IOException
import javax.lang.model.element.Modifier

object AdapterMethodBuilder {
    fun createReadMethodBuilder(returnTypeName: TypeName) = MethodSpecExt.overrideMethodBuilder("readImpl").apply {
        addModifiers(Modifier.PUBLIC)
        returns(returnTypeName)
        addParameter(JsonReader::class.java, Constants.IN)
        addException(IOException::class.java)
    }

    fun createWriteMethodBuilder(writtenValueTypeName: TypeName) = MethodSpecExt.overrideMethodBuilder("writeImpl").apply {
        addModifiers(Modifier.PUBLIC)
        addParameter(JsonWriter::class.java, Constants.OUT)
        addParameter(writtenValueTypeName, Constants.VALUE)
        addException(IOException::class.java)
    }
}