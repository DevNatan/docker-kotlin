package org.katan.yoki.model

import org.katan.yoki.model.Stream.Companion.StdErr
import org.katan.yoki.model.Stream.Companion.StdIn
import org.katan.yoki.model.Stream.Companion.StdOut
import org.katan.yoki.model.Stream.Companion.Unknown
import kotlin.jvm.*
import kotlinx.serialization.*

@Serializable
public data class Stream internal constructor(
    internal val code: Byte,
    public val name: String
) {

    public companion object {

        public val StdIn: Stream = Stream(0, "STDIN")
        public val StdOut: Stream = Stream(1, "STDOUT")
        public val StdErr: Stream = Stream(2, "STDERR")
        public val Unknown: Stream = Stream(-1, "UNKNOWN")

        @JvmStatic
        public fun typeOfOrNull(code: Byte): Stream? {
            return when (code) {
                StdIn.code -> StdIn
                StdOut.code -> StdOut
                StdErr.code -> StdErr
                else -> null
            }
        }
    }
}

/**
 * Returns a [Stream] from the given name.
 *
 * @param name The name of the stream.
 */
public fun stream(name: String): Stream {
    return when (name) {
        StdIn.name -> StdIn
        StdOut.name -> StdOut
        StdErr.name -> StdErr
        else -> Unknown
    }
}
