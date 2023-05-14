package hegemonies.niopostgreskafkaconnect.repository

import hegemonies.niopostgreskafkaconnect.consts.TableName
import hegemonies.niopostgreskafkaconnect.model.OutboxMeta
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface OutboxMetaRepository : CoroutineCrudRepository<OutboxMeta, Long> {

    @Query(
        """
            SELECT last_id FROM ${TableName.OUTBOX_META} LIMIT 1 FOR UPDATE
        """
    )
    suspend fun blockLastId(): Long

    @Query("UPDATE ${TableName.OUTBOX_META} SET last_id = :lastId")
    suspend fun update(lastId: Long): Int
}
