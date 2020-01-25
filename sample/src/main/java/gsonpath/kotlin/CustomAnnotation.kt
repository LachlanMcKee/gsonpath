package gsonpath.kotlin

import gsonpath.AutoGsonAdapter
import gsonpath.GsonFieldValidationType

@Retention(AnnotationRetention.RUNTIME)
@AutoGsonAdapter(fieldValidationType = [GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL])
annotation class CustomAnnotation