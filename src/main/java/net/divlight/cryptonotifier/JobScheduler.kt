package net.divlight.cryptonotifier

import org.quartz.CronScheduleBuilder.dailyAtHourAndMinute
import org.quartz.JobBuilder.newJob
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory

open class JobScheduler {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            StdSchedulerFactory.getDefaultScheduler().run {
                start()
                scheduleJob(
                    newJob(NotifierJob::class.java).build(),
                    newTrigger().startNow()
                        .withSchedule(dailyAtHourAndMinute(0, 0))
                        .withSchedule(dailyAtHourAndMinute(12, 0))
                        .build()
                )
            }
        }
    }
}
