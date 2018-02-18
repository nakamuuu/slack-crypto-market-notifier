package net.divlight.cryptonotifier.bitflyer

import java.util.*

data class Execution(
    val id: Long,
    val side: String,
    val price: Int,
    val size: Double,
    val execDate: Date,
    val buyChildOrderAcceptanceId: String,
    val sellChildOrderAcceptanceId: String
)
