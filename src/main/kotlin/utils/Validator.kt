package utils

import entities.getUserId
import entities.isCarParkedNow
import entities.isTokenValid

enum class ValidationResult {
    INV_TOKEN, INV_NUM, NEW_USER, PARKED, NOT_PARKED
}

object Validator {

    fun validate(carNumber: String, token: String?): ValidationResult {
        if (!carNumber.matches("""^[ABEKMHOPCTYX]\d{3}[ABEKMHOPCTYX]{2}\d{2,3}${'$'}""".toRegex()))
            return ValidationResult.INV_NUM
        val userId = getUserId(carNumber)
        return if (userId == null)
            ValidationResult.NEW_USER
        else if (token == null || !isTokenValid(userId, token))
            ValidationResult.INV_TOKEN
        else if (isCarParkedNow(userId))
            ValidationResult.PARKED
        else ValidationResult.NOT_PARKED
    }




}