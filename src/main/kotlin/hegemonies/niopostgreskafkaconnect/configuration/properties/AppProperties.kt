package hegemonies.niopostgreskafkaconnect.configuration.properties

import hegemonies.niopostgreskafkaconnect.model.AppQueue
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppProperties(
    val queue: AppQueue
)
