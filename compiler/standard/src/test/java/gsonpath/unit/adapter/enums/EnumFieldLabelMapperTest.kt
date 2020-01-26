package gsonpath.unit.adapter.enums

import com.google.gson.FieldNamingPolicy
import gsonpath.adapter.enums.EnumFieldLabelMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class EnumFieldLabelMapperTest(
        private val input: String,
        private val output: Output) {

    @Test
    fun execute() {
        assertEquals(
                output.identityValue,
                EnumFieldLabelMapper.map(input, FieldNamingPolicy.IDENTITY))

        assertEquals(
                output.upperCamelCaseValue,
                EnumFieldLabelMapper.map(input, FieldNamingPolicy.UPPER_CAMEL_CASE))

        assertEquals(
                output.upperCamelCaseWithSpacesValue,
                EnumFieldLabelMapper.map(input, FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES))

        assertEquals(
                output.lowerCaseWithDashesValue,
                EnumFieldLabelMapper.map(input, FieldNamingPolicy.LOWER_CASE_WITH_DASHES))

        assertEquals(
                output.lowerCaseWithUnderscoresValue,
                EnumFieldLabelMapper.map(input, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES))
    }

    class Output(
            val identityValue: String,
            val upperCamelCaseValue: String,
            val upperCamelCaseWithSpacesValue: String,
            val lowerCaseWithDashesValue: String,
            val lowerCaseWithUnderscoresValue: String)

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> = listOf(
                arrayOf(
                        "EXAMPLE",
                        Output(
                                identityValue = "EXAMPLE",
                                upperCamelCaseValue = "Example",
                                upperCamelCaseWithSpacesValue = "Example",
                                lowerCaseWithDashesValue = "example",
                                lowerCaseWithUnderscoresValue = "example"
                        )
                ),
                arrayOf(
                        "EXAMPLE_VALUE",
                        Output(
                                identityValue = "EXAMPLE_VALUE",
                                upperCamelCaseValue = "ExampleValue",
                                upperCamelCaseWithSpacesValue = "Example Value",
                                lowerCaseWithDashesValue = "example-value",
                                lowerCaseWithUnderscoresValue = "example_value"
                        )
                ),
                arrayOf(
                        "EXAMPLE_1",
                        Output(
                                identityValue = "EXAMPLE_1",
                                upperCamelCaseValue = "Example1",
                                upperCamelCaseWithSpacesValue = "Example 1",
                                lowerCaseWithDashesValue = "example-1",
                                lowerCaseWithUnderscoresValue = "example_1"
                        )
                ),
                arrayOf(
                        "____EXAMPLE_____1____",
                        Output(
                                identityValue = "____EXAMPLE_____1____",
                                upperCamelCaseValue = "Example1",
                                upperCamelCaseWithSpacesValue = "Example 1",
                                lowerCaseWithDashesValue = "example-1",
                                lowerCaseWithUnderscoresValue = "example_1"
                        )
                )
        )
    }
}
