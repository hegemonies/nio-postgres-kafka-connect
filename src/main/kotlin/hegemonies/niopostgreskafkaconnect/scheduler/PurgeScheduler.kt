package hegemonies.niopostgreskafkaconnect.scheduler

import hegemonies.niopostgreskafkaconnect.configuration.properties.ConnectorProperties
import hegemonies.niopostgreskafkaconnect.service.PurgeService
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PurgeScheduler(
    private val connectorProperties: ConnectorProperties,
    private val purgeService: PurgeService,
) {
    @Scheduled(cron = "\${connector.purge-policy.purge-by-scheduler-policy.period}")
    fun purge() =
        runBlocking {
            if (connectorProperties.purgePolicy.enabled) {
                purgeService.purge()
            }
        }
}
