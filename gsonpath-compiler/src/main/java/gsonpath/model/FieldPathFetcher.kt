package gsonpath.model

object FieldPathFetcher {
    fun getJsonFieldPath(fieldInfo: FieldInfo, metadata: GsonObjectMetadata): FieldPath {
        val serializedName = SerializedNameFetcher.getSerializedName(fieldInfo, metadata.flattenDelimiter)
        val path = if (serializedName != null && serializedName.isNotBlank()) {
            if (metadata.pathSubstitutions.isNotEmpty()) {

                // Check if the serialized name needs any values to be substituted
                metadata.pathSubstitutions.fold(serializedName) { fieldPath, substitution ->
                    fieldPath.replace("{${substitution.original}}", substitution.replacement)
                }

            } else {
                serializedName
            }

        } else {
            // Since the serialized annotation wasn't specified, we need to apply the naming policy instead.
            FieldNamingPolicyMapper.applyFieldNamingPolicy(metadata.gsonFieldNamingPolicy, fieldInfo.fieldName)
        }

        return if (path.contains(metadata.flattenDelimiter)) {
            FieldPath.Nested(
                    if (path.last() == metadata.flattenDelimiter) {
                        path + fieldInfo.fieldName
                    } else {
                        path
                    }
            )
        } else {
            FieldPath.Standard(path)
        }
    }
}