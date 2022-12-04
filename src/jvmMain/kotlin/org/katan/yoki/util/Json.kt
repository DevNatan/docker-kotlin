@file:JvmName("JsonJvm")

package org.katan.yoki.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json: Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    allowStructuredMapKeys = true
}

public actual fun toJsonEncodedString(value: Any): String {
    return json.encodeToString(value)
}

public actual fun fromJsonEncodedString(value: String): Map<String, String?> {
    return json.decodeFromString(value)
}
