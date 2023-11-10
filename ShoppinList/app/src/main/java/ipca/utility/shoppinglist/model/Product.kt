package ipca.utility.shoppinglist.model

import org.json.JSONObject

data class Product (
    var id          : String,
    var name        : String,
    var qtt         : Int,
    var isChecked   : Boolean) {

    companion object{

        fun fromJson(jsonObject: JSONObject) : Product {
            return Product(
                jsonObject.get("id"         ) as String,
                jsonObject.get("name"       ) as String,
                jsonObject.get("qtt"        ) as Int,
                jsonObject.get("isChecked"  ) as Boolean)
        }

    }

}