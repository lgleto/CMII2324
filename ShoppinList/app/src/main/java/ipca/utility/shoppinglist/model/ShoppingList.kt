package ipca.utility.shoppinglist.model

import com.google.firebase.firestore.DocumentSnapshot

data class ShoppingList (
    var id : String,
    var name : String
){

    companion object{
        fun fromSnapshot(id : String, snapshot: Map<String,Any>) : ShoppingList{
            return ShoppingList(id,
                snapshot.get("name") as String
            )
        }
    }
}