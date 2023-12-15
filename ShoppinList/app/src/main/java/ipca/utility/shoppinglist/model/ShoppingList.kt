package ipca.utility.shoppinglist.model

import com.google.firebase.firestore.DocumentSnapshot

data class ShoppingList (
    var id : String,
    var name : String,
    var pathToImage : String?,
){


    fun toHasMap() : Map<String, Any?>{
        return hashMapOf(
            "name" to name,
            "pathToImage" to pathToImage,
        )
    }

    companion object{
        fun fromSnapshot(id : String, snapshot: Map<String,Any>) : ShoppingList{
            return ShoppingList(id,
                snapshot.get("name") as String,
                snapshot.get("pathToImage") as? String?
            )
        }
    }
}