package hegemonies.niopostgreskafkaconnect.model

import hegemonies.niopostgreskafkaconnect.consts.TableName
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = TableName.OUTBOX)
data class OutboxMessage(
    @Id
    val id: Long? = null,
    val createdAt: Instant,
    val topic: String,
    val partition: Int?,
    val key: String,
    val message: String
)
