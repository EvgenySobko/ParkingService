package utils

import java.sql.Timestamp

object DateTimeUtil {

    fun getCurrentDateAndTime(): Long = Timestamp(System.currentTimeMillis()).toInstant().epochSecond

}