package gsonpath.generator

import gsonpath.ProcessingException
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import javax.lang.model.element.Element

fun processingExceptionMatcher(element: Element?, message: String): TypeSafeMatcher<ProcessingException> {
    return object : TypeSafeMatcher<ProcessingException>() {
        override fun describeTo(description: Description) {
        }

        override fun matchesSafely(item: ProcessingException): Boolean {
            return item.element == element && item.message == message
        }
    }
}