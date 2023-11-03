package ipca.utility.shoppinglist.model

class Product {
    var name : String
    var qtt : Int
    var isChecked : Boolean

    constructor(name: String, qtt: Int, isChecked: Boolean) {
        this.name = name
        this.qtt = qtt
        this.isChecked = isChecked
    }
}