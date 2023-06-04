package hegemonies.niopostgreskafkaconnect.service

import hegemonies.niopostgreskafkaconnect.model.OutboxMessage

interface ProducerService {
    suspend fun send(message: OutboxMessage)
}
