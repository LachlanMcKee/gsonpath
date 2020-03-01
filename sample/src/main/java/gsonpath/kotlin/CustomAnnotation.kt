package gsonpath.kotlin

import gsonpath.GsonFieldValidationType
import gsonpath.annotation.AutoGsonAdapter

@Retention(AnnotationRetention.RUNTIME)
@AutoGsonAdapter(fieldValidationType = [GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL])
annotation class CustomAnnotation