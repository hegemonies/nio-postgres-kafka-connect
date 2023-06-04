package hegemonies.niopostgreskafkaconnect.model.kmq

import kotlinx.serialization.Serializable

@Serializable
data class CreateQueueResponse(
    val result: String
)
