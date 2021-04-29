package entities

import com.google.gson.Gson
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import utils.DateTimeUtil
import utils.Respond

object Parking : Table("parking") {
    val id = integer("id").primaryKey().autoIncrement("public.parking_id_seq")
    val arrivalTime = long("arrival_time")
    val departureTime = long("departure_time").nullable()
    val totalCost = integer("total_cost")
    val userId = integer("user_id")
}

data class ParkingLot(
    val id: Int,
    val arrivalTime: Long,
    val departureTime: Long?,
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

fun isCarParkedNow(userId: Int): Boolean {
    var isParkedNow = false
    transaction {
        val carsParked = Parking.select { Parking.userId eq userId and (Parking.departureTime eq null) }
        if (!carsParked.empty()) isParkedNow = true
    }
    return isParkedNow
}

fun addParking(carNumber: String) {
    transaction {
        Parking.insert {
            it[arrivalTime] = DateTimeUtil.getCurrentDateAndTime()
            it[departureTime] = null
            it[userId] = getUserId(carNumber)!!
        }
    }
}

fun addParking(user: User) {
    transaction {
        Parking.insert {
            it[arrivalTime] = DateTimeUtil.getCurrentDateAndTime()
            it[departureTime] = null
            it[userId] = user.id
        }
    }
}

fun calculateSummary(carNumber: String): Int {
    val userId = getUserId(carNumber)!!
    var sum = -1
    transaction {
        addLogger(StdOutSqlLogger)
        var parkingId: Int
        var arrTime: Long
        Parking.select { Parking.userId eq userId and (Parking.departureTime eq null) }.forEach {
            arrTime = it[Parking.arrivalTime]
            parkingId = it[Parking.id]
            Parking.update({ Parking.id eq parkingId }) { p ->
                val depTime = DateTimeUtil.getCurrentDateAndTime()
                p[departureTime] = depTime
                val diff = ((depTime - arrTime) / 1000 / 60 / 60).toInt()
                sum = if (diff == 0) Respond.COST_PER_HOUR * 1
                else Respond.COST_PER_HOUR * diff
            }
            Parking.update({ Parking.id eq parkingId }) { p ->
                p[totalCost] = sum
            }
        }
    }
    return sum
}

fun calculateOdd(carNumber: String, sum: Int): Int {
    val userId = getUserId(carNumber)!!
    var odd = 0
    transaction {
        val parkings = Parking.select { Parking.userId eq userId }.toMutableList()
        parkings.sortByDescending { it[Parking.departureTime] }
        val costSum = parkings.first()[Parking.totalCost]
        odd = sum - costSum
    }
    return odd
}

fun getReport(): List<Respond.ReportItem> {
    val list = mutableListOf<Respond.ReportItem>()
    transaction {
        Parking.selectAll().map {
            val user = getUser(it[Parking.userId])!!
            list.add(
                Respond.ReportItem(
                    carNumber = user.carNumber,
                    arrivalTime = it[Parking.arrivalTime],
                    departureTime = it[Parking.departureTime],
                    totalCost = it[Parking.totalCost]
                )
            )
        }
    }
    return list
}