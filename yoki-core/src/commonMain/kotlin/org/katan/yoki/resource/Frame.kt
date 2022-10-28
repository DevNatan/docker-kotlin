package org.katan.yoki.resource

import kotlinx.serialization.Serializable

/**
 * @property value The content of the frame.
 * @property length The __total__ length of the frame's content.
 * @property stream The stream this frame belongs to.
 */
@Serializable
public data class Frame(
    public val value: String,
    public val length: Int,
    public val stream: Stream
)
