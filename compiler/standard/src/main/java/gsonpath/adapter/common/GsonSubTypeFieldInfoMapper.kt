package gsonpath.adapter.common

import gsonpath.ProcessingException
import gsonpath.adapter.util.NullableUtil
import gsonpath.util.MethodElementContent

class GsonSubTypeFieldInfoMapper {
    fun mapToFieldInfo(
            methodContent: MethodElementContent,
            jsonKeys: Array<String>): List<GsonSubTypeFieldInfo> {

        val parameters = methodContent.parameterElementContents
        if (parameters.size != jsonKeys.size) {
            throw ProcessingException("The parameters size does not match the json keys size", methodContent.element)
        }

        return parameters.zip(jsonKeys)
                .mapIndexed { index, (parameter, key) ->
                    val nonNullAnnotationExists = parameter.element.annotationMirrors
                            .any { NullableUtil.isNullableKeyword(it.annotationType.asElement().simpleName.toString()) }

                    val nullable = !nonNullAnnotationExists && !parameter.typeName.isPrimitive

                    GsonSubTypeFieldInfo(key, "subTypeElement$index", parameter.typeName, nullable)
                }
    }
}