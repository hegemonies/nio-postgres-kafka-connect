package hegemonies.niopostgreskafkaconnect.model.kmq

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResult(
    val code: String,
    val message: String,
    val stacktrace: String
)
