package net.divlight.cryptonotifier

import net.divlight.cryptonotifier.bitflyer.BitFlyerService
import net.divlight.cryptonotifier.slack.SlackService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory

class NotifierJob : Job {
    private val logger = LoggerFactory.getLogger(NotifierJob::class.java)
    private val bitFlyerService = BitFlyerService.createService()
    private val slackService = SlackService.createService()

    override fun execute(context: JobExecutionContext) {
        logger.info("NotifierJob executed.")

        val response = bitFlyerService.getExecutions("BTC_JPY").execute()
        if (response.isSuccessful) {
            response.body()?.let { executions ->
                val text = """:bitcoin: BTC/JPY : ${"¥%,d".format(executions[0].price)}"""
                slackService.postMessage(
                    BuildConfig.SLACK_TOKEN,
                    BuildConfig.SLACK_CHANNEL_NAME,
                    """[{"text": "$text", "color": "#f7941a"}]"""
                ).execute()
            }
        } else {
            val text = "Failed to get the execution history for BTC/JPY."
            slackService.postMessage(
                BuildConfig.SLACK_TOKEN,
                BuildConfig.SLACK_CHANNEL_NAME,
                """[{"text": "$text", "color": "#f7941a"}]"""
            ).execute()
        }
    }
}
