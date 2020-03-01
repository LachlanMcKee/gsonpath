package gsonpath.kotlin

import com.google.gson.annotations.SerializedName
import gsonpath.annotation.AutoGsonAdapter
import gsonpath.annotation.GsonSubtype
import gsonpath.annotation.GsonSubtypeGetter

@GsonSubtype(jsonKeys = ["type"])
annotation class TypeSubType

@TypeSubType
sealed class Type {

    companion object {
        @GsonSubtypeGetter
        @JvmStatic
        fun getSubType(type: String?): Class<out Type>? {
            return when (type) {
                "type1" -> Type1::class.java
                "type2" -> Type2::class.java
                "type3" -> Type3::class.java
                else -> null
            }
        }
    }

    @get:SerializedName("common.")
    abstract val name: String

    @AutoGsonAdapter
    class Type1(
            override val name: String,
            @SerializedName("specific.")
            val intTest: Int
    ) : Type()

    @AutoGsonAdapter
    class Type2(
            override val name: String,
            @SerializedName("specific.")
            val doubleTest: Double
    ) : Type()

    @AutoGsonAdapter
    class Type3(
            override val name: String,
            @SerializedName("specific.")
            val stringTest: String

    ) : Type()
}

@AutoGsonAdapter
class SealedClassArray(
        val value1: String?,

        val items: Array<Type>,

        val value2: String?
)

@AutoGsonAdapter
data class SealedClassPojo(
        val value1: String?,
        val item: Type,
        val value2: String?
)

@AutoGsonAdapter
class SealedClassSubTypePojo(
        val value1: String?,

        @SerializedName("items[0]")
        val item: Type,

        val value2: String?
)