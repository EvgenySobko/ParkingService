package utils

import com.google.gson.Gson
import org.joda.time.DateTime
import java.time.LocalDateTime
import java.util.*

object Respond {

    const val ICN = "Invalid car number"
    const val CAP = "Car with this number is already parked"
    const val NEM = "Not enough money"

    private data class ParkingTime(val date: String)
    private data class Summary(val sum: Int)
    private data class Odd(val odd: Int)
    private data class Report(
        val carNumber: String,
        val arrivalTime: DateTime,
        val departureTime: DateTime,
        val totalCost: Int
    )

    fun parkingTimeRespond(date: LocalDateTime): String = Gson().toJson(ParkingTime(date.toString()))

    fun summaryRespond(sum: Int): String = Gson().toJson(Summary(sum))

    fun oddRespond(odd: Int): String = Gson().toJson(Odd(odd))
}