package me.devnatan.yoki.protocol

public actual fun getEnvVar(name: String): String? = System.getenv(name)