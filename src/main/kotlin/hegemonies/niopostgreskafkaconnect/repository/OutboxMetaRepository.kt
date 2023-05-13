package hegemonies.niopostgreskafkaconnect.repository

import hegemonies.niopostgreskafkaconnect.consts.TableName
import hegemonies.niopostgreskafkaconnect.model.OutboxMeta
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface OutboxMetaRepository : CoroutineCrudRepository<OutboxMeta, Long> {

    @Query("UPDATE ${TableName.OUTBOX_META} SET last_id = :lastId")
    suspend fun update(lastId: Long): Int
}
