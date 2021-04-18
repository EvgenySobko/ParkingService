package utils

object Validator {

    fun validateCarNumber(carNumber: String) =
        carNumber.matches("""^[АВЕКМНОРСТУХ]\d{3}[АВЕКМНОРСТУХ]{2}\d{2,3}${'$'}""".toRegex())

}