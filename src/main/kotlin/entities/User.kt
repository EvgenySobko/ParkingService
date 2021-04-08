package entities

import com.google.gson.Gson
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("schema.user") {
    val id = text("id").primaryKey()
}

data class User(val id: String)

fun getUsers(): String {
    var json = ""
    transaction {
        val data = Users.selectAll()
        val list = mutableListOf<User>()
        data.map { list.add(User(it[Users.id])) }
        json = Gson().toJson(list)
    }
    return json
}

fun insertUsers() {

}