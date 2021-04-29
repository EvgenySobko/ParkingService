package utils

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

object Request {

    data class Car(
        @SerializedName("car") val carNumber: String
    )

    data class Payment(
        @SerializedName("car") val carNumber: String,
        @SerializedName("sum") val sum: Int
    )

    fun parking(json: String): Car = Gson().fromJson(json, Car::class.java)

    fun payment(json: String): Payment = Gson().fromJson(json, Payment::class.java)
}