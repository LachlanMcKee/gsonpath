package gsonpath.model

import gsonpath.generator.processingExceptionMatcher
import org.hamcrest.CoreMatchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class MutableGsonFieldTest {
    @JvmField
    @Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun testInvalidGsonArray() {
        expectedException.expect(`is`(processingExceptionMatcher(null, "Array should not be empty")))
        MutableGsonArray().toImmutable()
    }
}