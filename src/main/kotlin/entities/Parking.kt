package entities

import com.google.gson.Gson
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import utils.DateTimeUtil

object Parking : Table("schema.parking") {
    val id = integer("id").primaryKey().autoIncrement("schema.parking_id_seq")
    val arrivalTime = date("arrival_time")
    val departureTime = date("departure_time").nullable()
    val totalCost = integer("total_cost")
    val userId = integer("user_id")
}

data class ParkingLot(
    val id: Int,
    val arrivalTime: DateTime,
    val departureTime: DateTime?,
    val totalCost: Int,
    val userId: Int
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

fun addUser(carNum: String): Int {
    var id: Int = 0
    transaction {
        val r: Number? = Users.insert {
            it[carNumber] = carNum
        }.generatedKey
        id = r!!.toInt()
    }
    println(id)
    return id
}

fun getUserId(carNumber: String): Int {
    var id: Int? = null
    transaction {
        val row = Users.select { Users.carNumber eq carNumber }.limit(1)
        row.forEach { id = it[Users.id] }
    }
    return id ?: addUser(carNumber)
}

fun isCarParkedNow(carNumber: String): Boolean {
    var isParkedNow = false
    val id = getUserId(carNumber)
    transaction {
        val carsParked = Parking.select { Parking.userId eq id and (Parking.departureTime eq null) }
        if (!carsParked.empty()) isParkedNow = true
    }
    return isParkedNow
}

fun addParking(carNumber: String) {
    transaction {
        Parking.insert {
            it[arrivalTime] = DateTimeUtil.getCurrentDateAndTime()
            it[departureTime] = null
            it[userId] = getUserId(carNumber)
        }
    }
}