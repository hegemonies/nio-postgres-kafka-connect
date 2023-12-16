package hegemonies.niopostgreskafkaconnect.model

import hegemonies.niopostgreskafkaconnect.consts.TableName
import org.springframework.data.relational.core.mapping.Table

@Table(name = TableName.OUTBOX_META)
data class OutboxMeta(
    val lastId: Long,
)
