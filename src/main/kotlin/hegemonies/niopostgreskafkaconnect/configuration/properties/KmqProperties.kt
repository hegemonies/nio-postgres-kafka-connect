package hegemonies.niopostgreskafkaconnect.configuration.properties

import org.jetbrains.annotations.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kmq")
data class KmqProperties(
    @field:NotNull
    val address: String = "localhost",
    @field:NotNull
    val port: Int = 8080
)
