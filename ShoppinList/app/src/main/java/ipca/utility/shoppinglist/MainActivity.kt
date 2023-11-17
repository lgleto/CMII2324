package ipca.utility.shoppinglist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.utility.shoppinglist.databinding.ActivityMainBinding
import ipca.utility.shoppinglist.databinding.RowListBinding
import ipca.utility.shoppinglist.databinding.RowProductBinding
import ipca.utility.shoppinglist.model.Product
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

        binding.buttonAddProduct.setOnClickListener {
            val intent = Intent(this, ProductDetailActivity::class.java)
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

            return rootView.root
        }

    }
}