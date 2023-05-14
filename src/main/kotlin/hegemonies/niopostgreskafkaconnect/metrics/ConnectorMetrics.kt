package hegemonies.niopostgreskafkaconnect.metrics

import hegemonies.niopostgreskafkaconnect.repository.OutboxMetaRepository
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import mu.KLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@Component
class ConnectorMetrics(
    private val meterRegistry: MeterRegistry,
    private val outboxMetaRepository: OutboxMetaRepository
) {

    @EventListener(ApplicationReadyEvent::class)
    fun init() {
        meterRegistry.gauge(
            LAST_ID_METRIC_NAME,
            0L
        ) {
            runCatching {
                runBlocking { (outboxMetaRepository.findAll().toList().firstOrNull()?.lastId ?: 0).toDouble() }
            }.getOrElse { error ->
                logger.error(error) { "Failed to get lastId for metrics" }
                0.0
            }
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

    private companion object : KLogging() {
        const val LAST_ID_METRIC_NAME = "nio_connector_last_id"
        const val HANDLE_MESSAGE_METRIC_NAME = "nio_connector_handle_message"
        const val MESSAGE_COUNTER_METRIC_NAME = "nio_connector_message_counter"
    }
}
