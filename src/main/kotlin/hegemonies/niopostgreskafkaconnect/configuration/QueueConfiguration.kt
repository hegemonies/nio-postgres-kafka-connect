package hegemonies.niopostgreskafkaconnect.configuration

import hegemonies.niopostgreskafkaconnect.configuration.properties.AppProperties
import hegemonies.niopostgreskafkaconnect.model.AppQueue
import hegemonies.niopostgreskafkaconnect.service.KafkaService
import hegemonies.niopostgreskafkaconnect.service.KmqService
import hegemonies.niopostgreskafkaconnect.service.ProducerService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class QueueConfiguration(
    private val appProperties: AppProperties
) {

    @Bean
    fun producerService(
        kafkaTemplate: KafkaTemplate<String, String>,
        kmqClient: WebClient
    ): ProducerService =
        when (appProperties.queue) {
            AppQueue.KAFKA -> KafkaService(kafkaTemplate)
            AppQueue.KMQ -> KmqService(kmqClient)
        }
}
