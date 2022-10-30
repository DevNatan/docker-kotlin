package org.katan.yoki.resource

import kotlinx.serialization.Serializable

@Serializable
public data class ProcessConfig internal constructor(
    val privileged: Boolean,
    val user: String,
    val tty: Boolean,
    val entrypoint: String,
    val arguments: List<String>,
)
