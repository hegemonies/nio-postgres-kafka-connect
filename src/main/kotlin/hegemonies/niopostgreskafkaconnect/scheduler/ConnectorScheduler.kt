package hegemonies.niopostgreskafkaconnect.scheduler

import hegemonies.niopostgreskafkaconnect.service.ConnectService
import kotlinx.coroutines.runBlocking
import mu.KLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ConnectorScheduler(
    private val connectorService: ConnectService
) {

    @Scheduled(cron = "0/20 * * * * *") // every minute
    fun handle() = runBlocking {
        logger.info { "Start connector scheduler" }
        connectorService.collect()
        logger.info { "Finish connector scheduler" }
    }

    private companion object : KLogging()
}
