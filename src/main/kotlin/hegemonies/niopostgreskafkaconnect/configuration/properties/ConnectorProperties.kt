package hegemonies.niopostgreskafkaconnect.configuration.properties

import hegemonies.niopostgreskafkaconnect.configuration.properties.purgepolicy.PurgePolicy
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "connector")
data class ConnectorProperties(
    val purgePolicy: PurgePolicy? = null,
)
