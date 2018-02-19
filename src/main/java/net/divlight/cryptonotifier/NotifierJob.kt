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

        val responses = arrayOf(
            bitFlyerService.getExecutions("BTC_JPY").execute(),
            bitFlyerService.getExecutions("ETH_BTC").execute(),
            bitFlyerService.getExecutions("BCH_BTC").execute()
        )
        if (responses.any { it.isSuccessful }) {
            val btcPrice = responses[0].body()!![0].price
            val ethPrice = responses[1].body()!![0].price
            val bchPrice = responses[2].body()!![0].price
            val text = """
                |:bitcoin: BTC/JPY : ${"¥%,.0f".format(btcPrice)}
                |:ethereum: ETH/BTC : ${"%.5fBTC".format(ethPrice)} (${"¥%,.0f".format(ethPrice * btcPrice)})
                |:bitcoin-cash: BCH/BTC : ${"%.5fBTC".format(bchPrice)} (${"¥%,.0f".format(bchPrice * btcPrice)})
            """.trimMargin()
            slackService.postMessage(
                BuildConfig.SLACK_TOKEN,
                BuildConfig.SLACK_CHANNEL_NAME,
                """[{"text": "$text", "color": "#458ccb"}]"""
            ).execute()
        } else {
            val text = "Failed to get the execution histories."
            slackService.postMessage(
                BuildConfig.SLACK_TOKEN,
                BuildConfig.SLACK_CHANNEL_NAME,
                """[{"text": "$text", "color": "#c5c5c5"}]"""
            ).execute()
        }
    }
}
