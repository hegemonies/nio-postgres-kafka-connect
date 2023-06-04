package hegemonies.niopostgreskafkaconnect.model.kmq

import kotlinx.serialization.Serializable

@Serializable
data class CreateQueueRequest(
    val queueName: String,
    val capacity: Int,
    val persist: Boolean,
    val type: String,
    val ifNotExists: Boolean
)
