package gsonpath.adapter.standard.adapter.properties

import gsonpath.ProcessingException
import javax.lang.model.element.Element

class PropertyFetcher(private val adapterElement: Element) {

    fun <T> getProperty(
            propertyName: String,
            adapterArray: Array<T>,
            factoryProperty: T
    ): T {
        if (adapterArray.size > 1) {
            throw ProcessingException("$propertyName should never have more than one element", adapterElement)
        }

        return adapterArray.firstOrNull() ?: factoryProperty
    }
}