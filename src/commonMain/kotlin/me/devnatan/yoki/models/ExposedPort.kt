package me.devnatan.yoki.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
public data class ExposedPort internal constructor(
    public val port: UShort,
    public val protocol: ExposedPortProtocol = ExposedPortProtocol.TCP,
) {
    override fun toString(): String {
        return "$port/${protocol.toString().lowercase()}"
    }

    public companion object {
        public fun fromString(exposedPort: String): ExposedPort {
            val portAndProtocol = exposedPort.split('/')
            if (portAndProtocol.size != 2) error("Invalid exposed port")

            val port = portAndProtocol.getOrNull(0)?.toUShortOrNull() ?: error("Invalid exposed port")

            val protocolString = portAndProtocol.getOrNull(1) ?: error("Invalid exposed port")
            val protocol = runCatching { ExposedPortProtocol.valueOf(protocolString.uppercase()) }
                .getOrElse { error("Invalid exposed port") }

            return ExposedPort(port, protocol)
        }
    }
}

@Serializable
public enum class ExposedPortProtocol {
    @SerialName("tcp")
    TCP,

    @SerialName("udp")
    UDP,

    @SerialName("sctp")
    SCTP,
}

internal object ExposedPortSerializer : KSerializer<ExposedPort> {

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("ExposedPort") {
            element("port", UShort.serializer().descriptor)
            element("protocol", ExposedPortProtocol.serializer().descriptor)
        }

    override fun deserialize(decoder: Decoder): ExposedPort {
        try {
            return ExposedPort.fromString(decoder.decodeString())
        } catch (e: Throwable) {
            throw SerializationException("Cannot parse exposed port", e)
        }
    }

    override fun serialize(encoder: Encoder, value: ExposedPort) {
        encoder.encodeString(value.toString())
    }
}

internal object ExposedPortsSerializer :
    JsonTransformingSerializer<List<ExposedPort>>(ListSerializer(ExposedPortSerializer)) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonArray(element.jsonObject.entries.map { JsonPrimitive(it.key) })
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonArray.associate { it.jsonPrimitive.content to JsonObject(mapOf()) })
    }
}
