package entities

import com.google.gson.Gson
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import utils.Respond

object Users : Table("schema.user") {
    val id = integer("id").primaryKey().autoIncrement("schema.user_id_seq1")
    val carNumber = text("number")
}

data class User(val id: Int, val carNumber: String)

//fun getUsers(): String {
//    var json = ""
//    transaction {
//        val data = Users.selectAll()
//        val list = mutableListOf<User>()
//        data.map { list.add(User(it[Users.id])) }
//        json = Gson().toJson(list)
//    }
//    return json
//}

fun insertUsers() {

}