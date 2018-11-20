package gsonpath.kotlin

import com.google.gson.annotations.SerializedName
import gsonpath.AutoGsonAdapter
import gsonpath.GsonFieldValidationType

@AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL)
data class DataClassSample(
        @SerializedName("parent.child.")
        val value1: String,
        val isBooleanTest1: Boolean,
        val isBooleanTest2: Boolean?
)