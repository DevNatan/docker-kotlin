package org.katan.yoki.resource.container

import kotlinx.serialization.*

@Serializable
public data class ContainerListOptions(
    public var all: Boolean? = null,
    public var limit: Int? = null,
    public var size: Boolean? = null,
    public var filters: Filters? = null
) {

    @Serializable
    public data class Filters(
        public var ancestor: String? = null,
        public var before: String? = null,
        public var expose: String? = null, // TODO ExposedPort type
        public var exited: Int? = null,
        public var health: String? = null,
        public var id: String? = null,
        public var isolation: String? = null,
        @SerialName("is-task") public var isTask: Boolean? = null,
        public var label: String? = null,
        public var name: String? = null,
        public var network: String? = null,
        public var publish: String? = null, // TODO ExposedPort type
        public var since: String? = null,
        public var status: String? = null,
        public var volume: String? = null
    )

    public companion object {

        public const val Ancestor: String = "ancestor"
        public const val Before: String = "before"
        public const val Expose: String = "expose"
    }

}
