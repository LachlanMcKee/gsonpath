package gsonpath

import javax.lang.model.element.Element

/**
 * Represents that something has gone wrong during annotation processing.
 */
class ProcessingException constructor(override val message: String, val element: Element? = null) : Exception(message)
