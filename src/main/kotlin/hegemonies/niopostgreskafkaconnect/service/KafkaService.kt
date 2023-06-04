package hegemonies.niopostgreskafkaconnect.service

import hegemonies.niopostgreskafkaconnect.model.OutboxMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

class KafkaService(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : ProducerService {

    override suspend fun send(message: OutboxMessage) {
        val result = if (message.partition != null) {
            kafkaTemplate.send(message.topic, message.partition, message.key, message.message)
        } else {
            kafkaTemplate.send(message.topic, message.key, message.message)
        }

        result.get(1, TimeUnit.SECONDS)
    }
}
