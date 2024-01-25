package hegemonies.niopostgreskafkaconnect.repository

import hegemonies.niopostgreskafkaconnect.consts.TableName
import hegemonies.niopostgreskafkaconnect.model.OutboxMessage
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.Instant

interface OutboxRepository : CoroutineCrudRepository<OutboxMessage, Long> {
    @Query("SELECT * FROM ${TableName.OUTBOX} WHERE id > :lastId ORDER BY id")
    fun findAllByIdThatBigger(lastId: Long): Flow<OutboxMessage>

    @Query("DELETE FROM ${TableName.OUTBOX} WHERE created_at < :createdAt")
    suspend fun deleteAllBeforeCreatedAt(createdAt: Instant): Int
}
