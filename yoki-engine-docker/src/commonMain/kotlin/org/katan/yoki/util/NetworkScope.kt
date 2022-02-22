package org.katan.yoki.util

import org.katan.yoki.model.network.NetworkGlobalScope
import org.katan.yoki.model.network.NetworkLocalScope
import org.katan.yoki.model.network.NetworkSwarmScope

public fun requireNetworkScope(scope: String) {
    return require(scope == NetworkLocalScope || scope == NetworkGlobalScope || scope == NetworkSwarmScope) {
        "Network scope must be $NetworkLocalScope, $NetworkGlobalScope or $NetworkSwarmScope"
    }
}
