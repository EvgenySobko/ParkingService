package utils

object Validator {

    fun validateCarNumber(carNumber: String) =
        carNumber.matches("""^[ABEKMHOPCTYX]\d{3}[ABEKMHOPCTYX]{2}\d{2,3}${'$'}""".toRegex())

}