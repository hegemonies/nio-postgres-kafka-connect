package hegemonies.niopostgreskafkaconnect.configuration

import hegemonies.niopostgreskafkaconnect.configuration.properties.KmqProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class KmqConfiguration(
    private val kmqProperties: KmqProperties
) {

    @Bean
    fun kmqClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://${kmqProperties.address}:${kmqProperties.port}")
            .build()
    }
}
