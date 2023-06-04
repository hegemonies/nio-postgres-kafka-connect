package hegemonies.niopostgreskafkaconnect.model.kmq

import kotlinx.serialization.Serializable

@Serializable
data class SuccessResult(
    val success: Boolean
)
