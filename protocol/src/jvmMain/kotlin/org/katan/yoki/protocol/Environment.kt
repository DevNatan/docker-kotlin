package org.katan.yoki.protocol

public actual fun getEnvVar(name: String): String? = System.getenv(name)