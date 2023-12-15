package ipca.utility.shoppinglist.model

import com.google.firebase.firestore.DocumentSnapshot

data class User (
    var id : String,
    var name : String,
    var email : String?,
    var pathToImage : String?,
){


    fun toHasMap() : Map<String, Any?>{
        return hashMapOf(
            "name" to name,
            "email" to email,
            "pathToImage" to pathToImage,
        )
    }

    companion object{
        fun fromSnapshot(id : String, snapshot: Map<String,Any>) : User{
            return User(id,
                snapshot.get("name") as String,
                snapshot.get("email") as? String?,
                snapshot.get("pathToImage") as? String?
            )
        }
    }
}