package net.divlight.cryptonotifier

import org.quartz.CronScheduleBuilder.dailyAtHourAndMinute
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
                buildDailyAtHourAndMinuteTriggerWithJST(0, 0)
            )
            scheduler.scheduleJob(
                newJob(NotifierJob::class.java).build(),
                buildDailyAtHourAndMinuteTriggerWithJST(6, 0)
            )
            scheduler.scheduleJob(
                newJob(NotifierJob::class.java).build(),
                buildDailyAtHourAndMinuteTriggerWithJST(12, 0)
            )
            scheduler.scheduleJob(
                newJob(NotifierJob::class.java).build(),
                buildDailyAtHourAndMinuteTriggerWithJST(18, 0)
            )
        }

        private fun buildDailyAtHourAndMinuteTriggerWithJST(hour: Int, minute: Int) = newTrigger()
            .withSchedule(dailyAtHourAndMinute(hour, minute).inTimeZone(TimeZone.getTimeZone("GMT+9:00")))
            .build()
    }
}
