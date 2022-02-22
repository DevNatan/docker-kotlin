package org.katan.yoki.protocol

import kotlinx.coroutines.flow.Flow

public interface UnixSocket {

    public fun close()
}

public expect class SingleUnixSocket(socketPath: String) : UnixSocket {

    public val socketPath: String

    public override fun close()
}

public expect class PersistentUnixSocket(socketPath: String) : UnixSocket {

    public suspend fun write(value: String)

    public suspend fun read(): Flow<String>

    public override fun close()
}
