package gsonpath.extension.range

import com.squareup.javapoet.CodeBlock
import gsonpath.extension.addException
import gsonpath.util.`if`

/**
 * Creates the 'range' code validation code block.
 *
 * @param value the value being inspected
 * @param isFrom whether this is a 'from' or a 'to' range inspection
 * @param isInclusive whether the range validation is inclusive of the value
 * @param jsonPath the json path of the field being validated
 * @param variableName the name of the variable that is assigned back to the fieldName
 */
fun CodeBlock.Builder.handleRangeValue(value: String,
                                       isFrom: Boolean,
                                       isInclusive: Boolean,
                                       jsonPath: String,
                                       variableName: String): CodeBlock.Builder {

    val comparisonOperator: String =
            if (isFrom) {
                if (isInclusive) "<" else "<="
            } else {
                if (isInclusive) ">" else ">="
            }

    val expectedOperator: String =
            if (isFrom) {
                if (isInclusive) ">=" else ">"
            } else {
                if (isInclusive) "<=" else "<"
            }

    val label: String = if (isFrom) "from" else "to"

    return `if`("$variableName $comparisonOperator $value") {
        addException("Invalid '$label' range for JSON element '$jsonPath'. Expected: '$expectedOperator $value', " +
                """Found '" + $variableName + "'""")
    }
}