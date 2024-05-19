package me.devnatan.yoki.models

import kotlinx.serialization.Serializable
import me.devnatan.yoki.models.Stream.Companion.StdErr
import me.devnatan.yoki.models.Stream.Companion.StdIn
import me.devnatan.yoki.models.Stream.Companion.StdOut
import me.devnatan.yoki.models.Stream.Companion.Unknown
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

@Serializable
public data class Stream internal constructor(
    internal val code: Byte,
    public val name: String,
) {
    public companion object {
        @JvmStatic
        @get:JvmName("STDIN")
        public val StdIn: Stream = Stream(0, "STDIN")

        @JvmStatic
        @get:JvmName("STDOUT")
        public val StdOut: Stream = Stream(1, "STDOUT")

        @JvmStatic
        @get:JvmName("STDERR")
        public val StdErr: Stream = Stream(2, "STDERR")

        @JvmStatic
        @get:JvmName("UNKNOWN")
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

/**
 * @property value The content of the frame.
 * @property length The __total__ length of the frame's content.
 * @property stream The stream this frame belongs to.
 */
@Serializable
public data class Frame(
    public val value: String,
    public val length: Int,
    public val stream: Stream,
)
