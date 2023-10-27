package ipca.utility.calculator


enum class Operator {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE
}
class CalculatorBrain {

    var operator : Operator? =  null
    var operand : Double = 0.0

    fun doOperation(vaule : Double) : Double {
        var result =  operand
        when(operator){
            Operator.ADD      -> { result += vaule }
            Operator.SUBTRACT -> { result -= vaule }
            Operator.MULTIPLY -> { result *= vaule }
            Operator.DIVIDE   -> { result /= vaule }
            null -> { result = 0.0 }
        }
        return result
    }

}