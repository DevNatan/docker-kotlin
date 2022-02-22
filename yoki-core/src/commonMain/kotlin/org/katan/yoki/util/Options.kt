package org.katan.yoki.util

import kotlin.properties.*
import kotlin.reflect.*

public open class Options {

    internal val values: MutableMap<String, Any?> = hashMapOf()
}

public fun <T : Options, V> property(key: String, defaultValue: (() -> V) = { error("Undefined property") }): ReadWriteProperty<T, V> {
    return object : ReadWriteProperty<T, V> {
        override fun getValue(thisRef: T, property: KProperty<*>): V {
            @Suppress("UNCHECKED_CAST")
            return thisRef.values[key] as? V ?: defaultValue()
        }

        override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
            thisRef.values[key] = value
        }
    }
}

public fun Options.asSerializable(): Any = values.toMap()
