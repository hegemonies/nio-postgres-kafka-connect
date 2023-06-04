package hegemonies.niopostgreskafkaconnect.service

import hegemonies.niopostgreskafkaconnect.model.OutboxMessage
import hegemonies.niopostgreskafkaconnect.model.kmq.CreateQueueRequest
import org.springframework.web.reactive.function.client.WebClient

class KmqService(
    private val kmqClient: WebClient
) : ProducerService {

    override suspend fun send(message: OutboxMessage) {

    }

    private suspend fun createQueue(queueName: String) {
        val request = CreateQueueRequest(
            queueName = queueName,
            capacity = 100000,
            persist = true,
            type = "CHANNEL",
            ifNotExists =
        )
        kmqClient.post()
            .bodyValue()
    }
}
