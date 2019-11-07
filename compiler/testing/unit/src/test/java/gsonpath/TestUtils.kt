package gsonpath

import gsonpath.adapter.standard.model.GsonArray
import gsonpath.adapter.standard.model.GsonArrayElement
import gsonpath.adapter.standard.model.GsonModel
import gsonpath.adapter.standard.model.GsonObject

infix fun <A, B> A.toMap(that: B): Map<A, B> = mapOf(Pair(this, that))
fun emptyGsonObject() = GsonObject(emptyMap())
fun simpleGsonObject(key: String, model: GsonModel) = GsonObject(key toMap model)
fun simpleGsonArray(key: Int, model: GsonArrayElement) = GsonArray(key toMap model, key)