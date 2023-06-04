package hegemonies.niopostgreskafkaconnect.service

import hegemonies.niopostgreskafkaconnect.metrics.ConnectorMetrics
import hegemonies.niopostgreskafkaconnect.model.OutboxMessage
import hegemonies.niopostgreskafkaconnect.model.OutboxMeta
import hegemonies.niopostgreskafkaconnect.repository.OutboxMetaRepository
import hegemonies.niopostgreskafkaconnect.repository.OutboxRepository
import kotlinx.coroutines.flow.toList
import mu.KLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@Service
@OptIn(ExperimentalTime::class)
class ConnectService(
    private val outboxRepository: OutboxRepository,
    private val outboxMetaRepository: OutboxMetaRepository,
    private val transactionalOperator: TransactionalOperator,
    private val connectorMetrics: ConnectorMetrics,
    private val producerService: ProducerService
) {

    suspend fun collect() {
        val lastId = getLastId()

        outboxRepository.findAllByIdThatBigger(lastId)
            .collect { message ->
                logger.debug { "Message=$message" }
                handleMessage(message)
            }
    }

    private suspend fun getLastId(): Long {
        val meta = outboxMetaRepository.findAll().toList().firstOrNull()
            ?: outboxMetaRepository.save(OutboxMeta(0))
        return meta.lastId
    }

    private suspend fun handleMessage(message: OutboxMessage) {
        val elapsed = measureTime {
            transactionalOperator.executeAndAwait {
                message.id ?: throw RuntimeException("Failed to update outbox_meta set lastId=null")
                blockLastId(message.id)
                producerService.send(message)
                updateLastId(message.id)
            }
        }

        connectorMetrics.addHandleMessageMetric(elapsed)
        connectorMetrics.incrementMessageCounter()
    }

    private suspend fun blockLastId(lastId: Long) {
        val lastIdInDb = outboxMetaRepository.blockLastId()
        if (lastId != lastIdInDb + 1) {
            throw RuntimeException(
                "LastId=$lastIdInDb from database and current handling messageId=$lastId are not valid"
            )
        }
    }

    private suspend fun updateLastId(lastId: Long) {
        outboxMetaRepository.update(lastId)
    }

    private companion object : KLogging()
}
