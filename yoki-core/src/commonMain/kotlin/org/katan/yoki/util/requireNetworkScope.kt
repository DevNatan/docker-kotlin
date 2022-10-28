package org.katan.yoki.util

import org.katan.yoki.resource.network.NetworkGlobalScope
import org.katan.yoki.resource.network.NetworkLocalScope
import org.katan.yoki.resource.network.NetworkSwarmScope

public fun requireNetworkScope(scope: String) {
    return require(scope == NetworkLocalScope || scope == NetworkGlobalScope || scope == NetworkSwarmScope) {
        "Network scope must be $NetworkLocalScope, $NetworkGlobalScope or $NetworkSwarmScope"
    }
}
