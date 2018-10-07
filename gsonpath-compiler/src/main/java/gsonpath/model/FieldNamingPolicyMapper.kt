package gsonpath.model

import com.google.gson.FieldNamingPolicy
import gsonpath.ProcessingException
import java.lang.reflect.Field

class FieldNamingPolicyMapper {

    /**
     * Applies the gson field naming policy using the given field name.

     * @param fieldNamingPolicy the field naming policy to apply
     * *
     * @param fieldName         the name being altered.
     * *
     * @return the altered name.
     */
    @Throws(ProcessingException::class)
    fun applyFieldNamingPolicy(fieldNamingPolicy: FieldNamingPolicy, fieldName: String): String {
        //
        // Unfortunately the field naming policy uses a Field parameter to translate name.
        // As a result, for now it was decided to create a fake field class which supplies the correct name,
        // as opposed to copying the logic from GSON and potentially breaking compatibility if they add another enum.
        //
        val fieldConstructor = Field::class.java.declaredConstructors[0]
        fieldConstructor.isAccessible = true
        val fakeField: Field
        try {
            fakeField = fieldConstructor.newInstance(null, fieldName, null, -1, -1, null, null) as Field

        } catch (e: Exception) {
            throw ProcessingException("Error while creating 'fake' field for naming policy.")
        }

        // Applies the naming transformation on the input field name.
        return fieldNamingPolicy.translateName(fakeField)
    }
}