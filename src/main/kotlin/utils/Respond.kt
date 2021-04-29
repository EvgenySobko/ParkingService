package utils

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

object Respond {

    data class CarNumber(
        @SerializedName("car") val carNumber: String
    )

    data class Payment(
        @SerializedName("car") val carNumber: String,
        @SerializedName("sum") val sum: Int
    )

    const val COST_PER_HOUR = 150

    const val ICN = "Invalid car number"
    const val CNP = "Car with this number is not parked"
    const val CAP = "Car with this number is already parked"
    const val NEM = "Not enough money"

    data class ParkingTime(val date: String)
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

    fun parkingRequest(json: String): String = Gson().fromJson(json, CarNumber::class.java).carNumber

    fun paymentRequest(json: String): Payment = Gson().fromJson(json, Payment::class.java)

    fun historyRequest(json: String): Report = Gson().fromJson(json, Report::class.java)

    fun parkingTimeRespond(date: DateTime): String = Gson().toJson(ParkingTime(date.toString()))

    fun summaryRespond(sum: Int): String = Gson().toJson(Summary(sum))

    fun oddRespond(odd: Int): String = Gson().toJson(Odd(odd))
}