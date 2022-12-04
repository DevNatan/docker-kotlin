package me.devnatan.yoki.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Serializable
public data class HealthConfig(
    @SerialName("Test") public var test: List<String>? = null,
    @SerialName("Interval") public var interval: Int? = null,
    @SerialName("Timeout") public var timeout: Int? = null,
    @SerialName("Retries") public var retries: Int? = null,
    @SerialName("StartPeriod") public var startPeriod: Int? = null,
)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public var HealthConfig.interval: Duration?
    get() = interval?.toDuration(DurationUnit.NANOSECONDS)
    set(value) {
        interval = value?.inWholeNanoseconds?.toInt()
    }

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public var HealthConfig.timeout: Duration?
    get() = timeout?.toDuration(DurationUnit.NANOSECONDS)
    set(value) {
        timeout = value?.inWholeNanoseconds?.toInt()
    }

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public var HealthConfig.startPeriod: Duration?
    get() = startPeriod?.toDuration(DurationUnit.NANOSECONDS)
    set(value) {
        startPeriod = value?.inWholeNanoseconds?.toInt()
    }
