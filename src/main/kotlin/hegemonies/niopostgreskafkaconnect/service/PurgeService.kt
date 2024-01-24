package hegemonies.niopostgreskafkaconnect.service

import hegemonies.niopostgreskafkaconnect.repository.OutboxRepository
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import java.time.Instant

@Service
class PurgeService(
    private val outboxRepository: OutboxRepository,
    private val transactionalOperator: TransactionalOperator,
) {
    private companion object : KLogging()

    suspend fun purge() =
        transactionalOperator.executeAndAwait {
            logger.info { "Purging old data" }
            val countRows = outboxRepository.deleteAllBeforeCreatedAt(Instant.now())
            logger.info { "Deleted $countRows rows" }
        }

    suspend fun purgeById(id: Long) {
        logger.info { "Purge by id=$id" }
        outboxRepository.deleteById(id)
    }
}
