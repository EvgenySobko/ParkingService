package entities

import com.google.gson.Gson
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object Parking : Table("schema.parking") {
    val id = integer("id").primaryKey()
    val arrivalTime = date("arrival_time")
    val departureTime = date("departure_time")
    val totalCost = integer("total_cost")
    val userId = text("user_id")
}

data class ParkingLot(
    val id: Int,
    val arrivalTime: DateTime,
    val departureTime: DateTime,
    val totalCost: Int,
    val userId: String
)

fun getParking(): String {
    var json = ""
    transaction {
        val data = Parking.selectAll()
        val list = mutableListOf<ParkingLot>()
        data.map {
            list.add(
                ParkingLot(
                    id = it[Parking.id],
                    arrivalTime = it[Parking.arrivalTime],
                    departureTime = it[Parking.departureTime],
                    totalCost = it[Parking.totalCost],
                    userId = it[Parking.userId]
                )
            )
        }
        json = Gson().toJson(list)
    }
    return json
}