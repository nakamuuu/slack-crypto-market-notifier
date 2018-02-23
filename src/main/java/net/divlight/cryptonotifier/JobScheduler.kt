package net.divlight.cryptonotifier

import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder.newJob
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory
import java.util.*

open class JobScheduler {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val scheduler = StdSchedulerFactory.getDefaultScheduler()
            scheduler.start()
            scheduler.scheduleJob(
                newJob(NotifierJob::class.java).build(),
                newTrigger()
                    .withSchedule(
                        // Delay execution for 10 seconds to avoid SocketTimeoutException
                        cronSchedule("10 0 0,4,8,12,16,20 * * ?")
                            .inTimeZone(TimeZone.getTimeZone("GMT+9:00"))
                    )
                    .build()
            )
        }
    }
}
