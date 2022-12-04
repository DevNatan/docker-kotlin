package me.devnatan.yoki.util

public expect fun toJsonEncodedString(value: Any): String

public expect fun fromJsonEncodedString(value: String): Map<String, String?>
