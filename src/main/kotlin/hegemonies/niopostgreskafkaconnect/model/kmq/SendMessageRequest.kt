package hegemonies.niopostgreskafkaconnect.model.kmq

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val message: Message,
    val queueName: String
)