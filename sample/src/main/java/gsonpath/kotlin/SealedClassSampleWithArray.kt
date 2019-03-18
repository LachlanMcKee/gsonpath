package gsonpath.kotlin

import com.google.gson.annotations.SerializedName
import gsonpath.AutoGsonAdapter
import gsonpath.GsonSubtype

sealed class Type {
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

        @GsonSubtype(
                subTypeKey = "type",
                stringValueSubtypes = [
                    GsonSubtype.StringValueSubtype(value = "type1", subtype = Type.Type1::class),
                    GsonSubtype.StringValueSubtype(value = "type2", subtype = Type.Type2::class),
                    GsonSubtype.StringValueSubtype(value = "type3", subtype = Type.Type3::class)])
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
        @GsonSubtype(
                subTypeKey = "type",
                stringValueSubtypes = [
                    GsonSubtype.StringValueSubtype(value = "type1", subtype = Type.Type1::class),
                    GsonSubtype.StringValueSubtype(value = "type2", subtype = Type.Type2::class),
                    GsonSubtype.StringValueSubtype(value = "type3", subtype = Type.Type3::class)])
        val item: Type,

        val value2: String?
)