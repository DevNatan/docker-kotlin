package org.katan.yoki.engine.docker.model.container

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author João Victor Gomides Cruz
 */
@Serializable
public data class Mount(
    @SerialName("Target") public val target: String,
    @SerialName("Source") public val source: String,
    @SerialName("Type") public val type: String,
    @SerialName("ReadOnly") public val readOnly: Boolean,
    @SerialName("Consistency") public val consistency: String,
    @SerialName("BindOptions") public val bindOptions: BindOptions,
    @SerialName("VolumeOptions") public val volumeOptions: VolumeOptions,
    @SerialName("TmpfsOptions") public val tmpfsOptions: TmpfsOptions
) {
    @Serializable
    public data class BindOptions(
        @SerialName("Propagation") public val propagation: String,
        @SerialName("NonRecursive") public val nonRecursive: Boolean,
    )

    @Serializable
    public data class VolumeOptions(
        @SerialName("NoCopy") public val noCopy: Boolean,
        @SerialName("Labels") public val labels: Map<String, String>?,
        @SerialName("DriverConfig") public val driverConfig: DriverConfig
    ) {
        @Serializable
        public data class DriverConfig(
            @SerialName("Name") public val name: String,
            @SerialName("Options") public val options: Map<String, String>?
        )
    }

    @Serializable
    public data class TmpfsOptions(
        @SerialName("SizeBytes") public val sizeBytes: Long,
        @SerialName("Mode") public val mode: Int
    )
}
