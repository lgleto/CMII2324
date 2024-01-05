package ipca.utility.shoppinglist.model

import com.google.firebase.firestore.DocumentSnapshot
import ipca.utility.shoppinglist.removeAccentsLowerCase

data class ShoppingList (
    var id : String,
    var name : String,
    var pathToImage : String?,
    var nameSearch : String? = null,
){


    fun toHasMap() : Map<String, Any?>{
        return hashMapOf(
            "name" to name,
            "pathToImage" to pathToImage,
            "name_search" to name.removeAccentsLowerCase()
        )
    }

    companion object{
        fun fromSnapshot(id : String, snapshot: Map<String,Any>) : ShoppingList{
            return ShoppingList(id,
                snapshot.get("name") as String,
                snapshot.get("pathToImage") as? String?,
                snapshot.get("name_search") as? String?
            )
        }
    }
}