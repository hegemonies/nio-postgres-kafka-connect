package hegemonies.niopostgreskafkaconnect.metrics

import hegemonies.niopostgreskafkaconnect.repository.OutboxMetaRepository
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@Component
class ConnectorMetrics(
    private val meterRegistry: MeterRegistry,
    private val outboxMetaRepository: OutboxMetaRepository
) {

    private val lastIdStaticCounter by lazy {
        meterRegistry.gauge(
            LAST_ID_METRIC_NAME,
            AtomicLong()
        ) {
            runBlocking { (outboxMetaRepository.findAll().toList().firstOrNull()?.lastId ?: 0).toDouble() }
        }
    }

    private val handleMessageMetric by lazy {
        meterRegistry.timer(HANDLE_MESSAGE_METRIC_NAME)
    }

    private val messageCounterMetric by lazy {
        meterRegistry.counter(MESSAGE_COUNTER_METRIC_NAME)
    }

    fun addHandleMessageMetric(elapsed: Duration) {
        handleMessageMetric.record(elapsed.toJavaDuration())
    }

    fun incrementMessageCounter() {
        messageCounterMetric.increment()
    }

    private companion object {
        const val LAST_ID_METRIC_NAME = "nio-connector-last-id"
        const val HANDLE_MESSAGE_METRIC_NAME = "nio-connector-handle-message"
        const val MESSAGE_COUNTER_METRIC_NAME = "nio-connector-message-counter"
    }
}
