package me.devnatan.yoki.io

import kotlinx.io.RawSource

internal expect fun readTarFile(input: RawSource): RawSource

internal expect fun writeTarFile(filePath: String): RawSource
