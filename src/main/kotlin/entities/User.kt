package entities

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import utils.TokenGenerator.genToken

object Users : Table("public.user") {
    val id = integer("id").primaryKey().autoIncrement("public.user_id_seq1")
    val carNumber = text("number")
    val token = varchar("token", 32)
}

data class User(val id: Int, val carNumber: String, val token: String)

fun getUser(userId: Int): User? {
    var user: User? = null
    transaction {
        addLogger(StdOutSqlLogger)
        Users.select { Users.id eq userId }.map {
            user = User(it[Users.id], it[Users.carNumber], it[Users.token])
        }
    }
    return user
}

fun addNewUser(carNum: String): User {
    var id: Int = 0
    val t = genToken()
    transaction {
        addLogger(StdOutSqlLogger)
        val r: Number? = Users.insert {
            it[carNumber] = carNum
            it[token] = t
        }.generatedKey
        id = r!!.toInt()
    }
    return User(id, carNum, t)
}

fun getUserId(carNumber: String): Int? {
    var id: Int? = null
    transaction {
        addLogger(StdOutSqlLogger)
        val row = Users.select { Users.carNumber eq carNumber }.limit(1)
        row.forEach { id = it[Users.id] }
    }
    return id
}

fun isTokenValid(userId: Int, userToken: String): Boolean {
    var token: String? = null
    transaction {
        addLogger(StdOutSqlLogger)
        token = Users.slice(Users.token).select { Users.id eq userId }.map { it[Users.token] }.first()
    }
    println("Correct token is $token, sent $userToken")
    return userToken == token
}

fun isTokenAlreadyUsed(token: String): Boolean {
    var res = false
    transaction {
        addLogger(StdOutSqlLogger)
        res = Users.selectAll().map { it[Users.token] }.contains(token)
    }
    return res
}
