package hegemonies.niopostgreskafkaconnect.configuration.properties.purgepolicy

data class PurgeBySchedulerPolicy(
    // every 10 minutes by default
    val period: String = "0 */10 * * * *",
)
