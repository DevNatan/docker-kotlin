package me.devnatan.yoki.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json: Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    allowStructuredMapKeys = true
}

public fun toJsonEncodedString(value: Any): String {
    return json.encodeToString(value)
}

public fun fromJsonEncodedString(value: String): Map<String, String?> {
    return json.decodeFromString(value)
}
