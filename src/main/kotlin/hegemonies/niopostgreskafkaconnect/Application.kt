package hegemonies.niopostgreskafkaconnect

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableKafka
@EnableScheduling
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
