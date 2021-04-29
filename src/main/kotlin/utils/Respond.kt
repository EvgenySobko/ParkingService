package utils

import com.google.gson.Gson
import org.joda.time.DateTime

object Respond {

    const val COST_PER_HOUR = 150

    const val ICN = "Invalid car number"
    const val IUT = "Invalid user token"
    const val CNP = "Car with this number is not parked"
    const val CAP = "Car with this number is already parked"
    const val NEM = "Not enough money"

    data class ParkingTime(val date: String)
    data class ParkingTimeWithToken(val date: String, val token: String)
    data class Summary(val sum: Int)
    data class Odd(val odd: Int)
    data class ReportItem(
        val carNumber: String,
        val arrivalTime: DateTime,
        val departureTime: DateTime,
        val totalCost: Int
    )
    data class Report(
        val lines: List<ReportItem>
    )

    fun parkingTime(date: DateTime): String = Gson().toJson(ParkingTime(date.toString()))

    fun parkingTimeWithToken(date: DateTime, token: String): String = Gson().toJson(ParkingTimeWithToken(date.toString(), token))

    fun summary(sum: Int): String = Gson().toJson(Summary(sum))

    fun odd(odd: Int): String = Gson().toJson(Odd(odd))

    fun historyRespond(reportLines: List<ReportItem>): String  = Gson().toJson(Report(reportLines))
}