package ipca.utility.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.utility.shoppinglist.databinding.ActivityShoppongListDetailBinding
import ipca.utility.shoppinglist.model.ShoppingList

class ShoppingListDetailActivity : AppCompatActivity() {

    val db = Firebase.firestore

    private lateinit var binding: ActivityShoppongListDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppongListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonDone.setOnClickListener {
            addShoppingList(ShoppingList("",
                binding.editTextListName.text.toString()
            ))
        }


    }

    fun addShoppingList(shoppingList: ShoppingList){

        val data = shoppingList.toHasMap()

        db.collection("shoppingLists")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                Toast.makeText(this, "List added", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                Toast.makeText(this, "No internet!", Toast.LENGTH_LONG).show()
            }
    }

}