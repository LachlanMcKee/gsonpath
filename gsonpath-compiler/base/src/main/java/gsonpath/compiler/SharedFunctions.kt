package gsonpath.compiler

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName

val CLASS_NAME_STRING: ClassName = ClassName.get(String::class.java)

fun createDefaultVariableValueForTypeName(typeName: TypeName) = when (typeName) {
    TypeName.INT, TypeName.BYTE, TypeName.SHORT -> "0"
    TypeName.LONG -> "0L"
    TypeName.FLOAT -> "0f"
    TypeName.DOUBLE -> "0d"
    TypeName.CHAR -> "'\\u0000'"
    TypeName.BOOLEAN -> "false"
    else -> "null"
}

fun generateClassName(className: ClassName, classNameSuffix: String): String {
    //
    // We need to ensure that nested classes are have include their parent class as part of the name.
    // Otherwise this could cause file name contention when other nested classes have the same name
    //
    val fileName = className.simpleNames().joinToString("_")

    // Make sure no '.' managed to sneak through!
    return fileName.replace(".", "_") + "_" + classNameSuffix
}