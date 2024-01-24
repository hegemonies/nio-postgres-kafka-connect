package hegemonies.niopostgreskafkaconnect.configuration

import mu.KLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class SchedulerConfiguration {
    private companion object : KLogging()

    @Bean
    fun threadPoolTaskScheduler(): ThreadPoolTaskScheduler =
        ThreadPoolTaskScheduler().apply {
            poolSize = 2
            setThreadNamePrefix("thread-pool-task-scheduler")
            setErrorHandler { error ->
                logger.error(error) { "Scheduler job execution error" }
            }
        }
}
