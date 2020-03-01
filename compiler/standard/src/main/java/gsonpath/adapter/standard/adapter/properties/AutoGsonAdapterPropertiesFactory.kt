package gsonpath.adapter.standard.adapter.properties

import gsonpath.GsonFieldValidationType
import gsonpath.LazyFactoryMetadata
import gsonpath.ProcessingException
import gsonpath.annotation.AutoGsonAdapter
import javax.lang.model.element.TypeElement

class AutoGsonAdapterPropertiesFactory(
        private val adapterCommonPropertiesFactory: AdapterCommonPropertiesFactory
) {

    @Throws(ProcessingException::class)
    fun create(
            modelElement: TypeElement,
            adapterAnnotation: AutoGsonAdapter,
            lazyFactoryMetadata: LazyFactoryMetadata,
            isInterface: Boolean): AutoGsonAdapterProperties {

        val commonProperties = adapterCommonPropertiesFactory
                .create(modelElement, adapterAnnotation, lazyFactoryMetadata)

        val finalFieldValidationType = if (isInterface) {
            // Interfaces must use field validation to prevent issues with primitives.
            when (commonProperties.fieldValidationType) {
                GsonFieldValidationType.NO_VALIDATION ->
                    GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL

                else ->
                    commonProperties.fieldValidationType
            }
        } else {
            commonProperties.fieldValidationType
        }

        val pathSubstitutions = adapterAnnotation.substitutions

        // Validate the path substitutions. Duplicate keys are not allowed.
        pathSubstitutions.fold(emptySet()) { set: Set<String>, pathSubstitution ->
            if (set.contains(pathSubstitution.original)) {
                throw ProcessingException("PathSubstitution original values must be unique", modelElement)
            }
            set.plus(pathSubstitution.original)
        }

        return AutoGsonAdapterProperties(
                adapterAnnotation.ignoreNonAnnotatedFields,
                commonProperties.flattenDelimiter,
                commonProperties.serializeNulls,
                adapterAnnotation.rootField,
                finalFieldValidationType,
                commonProperties.fieldNamingPolicy,
                pathSubstitutions.toList())
    }
}
