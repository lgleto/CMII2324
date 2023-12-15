package ipca.utility.shoppinglist

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ipca.utility.shoppinglist.databinding.ActivityMainBinding
import ipca.utility.shoppinglist.databinding.RowListBinding
import ipca.utility.shoppinglist.model.ShoppingList


const val TAG = "shoppinglist"
class MainActivity : AppCompatActivity() {

    var shoppingLists = arrayListOf<ShoppingList>()

    private lateinit var binding: ActivityMainBinding
    private  var  adpapter = ShoppingListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listViewProducts.adapter = adpapter

        binding.buttonAddList.setOnClickListener {
            val intent = Intent(this, ShoppingListDetailActivity::class.java)
            startActivity(intent)
        }

        val db = Firebase.firestore
        db.collection("shoppingLists")
            .addSnapshotListener { snapshoot, error ->
                snapshoot?.documents?.let {
                    this.shoppingLists.clear()
                    for (document in it) {
                        document.data?.let{ data ->
                            this.shoppingLists.add(
                                ShoppingList.fromSnapshot(
                                    document.id,
                                    data
                                )
                            )
                        }
                    }
                    this.adpapter.notifyDataSetChanged()
                }


            }


    }

    inner class ShoppingListAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return shoppingLists.size
        }

        override fun getItem(position: Int): Any {
            return shoppingLists[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = RowListBinding.inflate(layoutInflater)
            rootView.textViewListName.text = shoppingLists[position].name

            shoppingLists[position].pathToImage?.let {
                val storage = Firebase.storage
                val storageRef = storage.reference
                val pathReference = storageRef.child(it)
                val ONE_MEGABYTE: Long = 10 * 1024 * 1024
                pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { data ->
                    val bitmap = BitmapFactory.decodeByteArray(data, 0 , data.count())
                    rootView.imageViewPhotoList.setImageBitmap(bitmap)
                }.addOnFailureListener {
                    // Handle any errors
                }

            }


            return rootView.root
        }

    }
}