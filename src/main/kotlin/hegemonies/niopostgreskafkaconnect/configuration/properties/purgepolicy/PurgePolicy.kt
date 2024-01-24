package hegemonies.niopostgreskafkaconnect.configuration.properties.purgepolicy

data class PurgePolicy(
    val enabled: Boolean = false,
    val purgeBySchedulerPolicy: PurgeBySchedulerPolicy? = null,
    val purgeAfterSendPolicy: PurgeAfterSendPolicy? = null,
)
