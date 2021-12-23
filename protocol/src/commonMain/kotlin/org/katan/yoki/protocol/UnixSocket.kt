package org.katan.yoki.protocol

import kotlinx.coroutines.flow.Flow

public interface UnixSocket : Closeable {

    public override fun close()

}

public expect class SingleUnixSocket : UnixSocket {

    public val socketPath: String

    override fun close()

}

public expect class PersistentUnixSocket : UnixSocket {

    public suspend fun write(value: String)

    public suspend fun read(): Flow<String>

    override fun close()

}
