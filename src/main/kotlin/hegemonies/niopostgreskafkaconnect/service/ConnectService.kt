package hegemonies.niopostgreskafkaconnect.service

import hegemonies.niopostgreskafkaconnect.metrics.ConnectorMetrics
import hegemonies.niopostgreskafkaconnect.model.OutboxMessage
import hegemonies.niopostgreskafkaconnect.model.OutboxMeta
import hegemonies.niopostgreskafkaconnect.repository.OutboxMetaRepository
import hegemonies.niopostgreskafkaconnect.repository.OutboxRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.future.await
import mu.KLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime

@Service
class ConnectService(
    private val outboxRepository: OutboxRepository,
    private val outboxMetaRepository: OutboxMetaRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val transactionalOperator: TransactionalOperator,
    private val connectorMetrics: ConnectorMetrics,
) {
    suspend fun collect() {
        val lastId = getLastId()
        logger.debug { "Find last_id = $lastId" }

        outboxRepository.findAllByIdThatBigger(lastId)
            .collect { message ->
                logger.debug { "Message=$message" }
                handleMessage(message)
            }
    }

    private suspend fun getLastId(): Long {
        val meta =
            outboxMetaRepository.findAll().toList().firstOrNull()
                ?: outboxMetaRepository.save(OutboxMeta(lastId = 0))
        return meta.lastId
    }

    private suspend fun handleMessage(message: OutboxMessage) {
        val elapsed =
            measureTime {
                transactionalOperator.executeAndAwait {
                    message.id ?: throw RuntimeException("Failed to update outbox_meta set lastId=null")
                    blockLastId(message.id)
                    sendMessageToKafka(message)
                    updateLastId(message.id)
                }
            }

        connectorMetrics.addHandleMessageMetric(elapsed)
        connectorMetrics.incrementMessageCounter()
    }

    private suspend fun sendMessageToKafka(message: OutboxMessage) {
        logger.debug { "Send message to kafka, id = ${message.id}" }
        val result =
            if (message.partition != null) {
                kafkaTemplate.send(message.topic, message.partition, message.key, message.message)
            } else {
                kafkaTemplate.send(message.topic, message.key, message.message)
            }

        result.orTimeout(1, TimeUnit.SECONDS).await()
    }

    private suspend fun blockLastId(lastId: Long) {
        logger.debug { "Block last_id = $lastId" }
        val lastIdInDb = outboxMetaRepository.blockLastId()
        if (lastId != lastIdInDb + 1) {
            throw RuntimeException(
                "LastId=$lastIdInDb from database and current handling messageId=$lastId are not valid",
            )
        }
    }

    private suspend fun updateLastId(lastId: Long) {
        logger.debug { "Update last_id = $lastId" }
        outboxMetaRepository.update(lastId)
    }

    private companion object : KLogging()
}
