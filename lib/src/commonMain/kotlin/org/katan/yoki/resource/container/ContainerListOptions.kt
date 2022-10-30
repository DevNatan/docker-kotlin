package org.katan.yoki.resource.container

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    ) {

        public companion object {

            public const val ANCESTOR: String = "ancestor"
            public const val BEFORE: String = "before"
            public const val EXPOSE: String = "expose"
            public const val EXITED: String = "exited"
            public const val HEALTH: String = "health"
            public const val ID: String = "id"
            public const val ISOLATION: String = "isolation"
            public const val IS_TASK: String = "is-task"
            public const val LABEL: String = "label"
            public const val NAME: String = "name"
            public const val NETWORK: String = "network"
            public const val PUBLISH: String = "publish"
            public const val SINCE: String = "since"
            public const val STATUS: String = "status"
            public const val VOLUME: String = "volume"
        }
    }
}
