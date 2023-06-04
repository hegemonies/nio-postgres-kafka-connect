package hegemonies.niopostgreskafkaconnect.model.kmq

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageResponse(
    val result: String
)
