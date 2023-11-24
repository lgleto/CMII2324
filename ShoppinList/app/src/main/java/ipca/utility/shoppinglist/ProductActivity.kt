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
import ipca.utility.shoppinglist.databinding.RowProductBinding
import ipca.utility.shoppinglist.model.Product


class ProductActivity : AppCompatActivity() {

    var products = arrayListOf<Product>()

    private lateinit var binding: ActivityMainBinding
    private  var  adpapter = ProductAdapter()
    val resultLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            it.data?.let { data ->
                val name = data.getStringExtra(ProductDetailActivity.EXTRA_NAME)?:""
                val qtt = data.getIntExtra(ProductDetailActivity.EXTRA_QTT,0)
                val position = data.getIntExtra(ProductDetailActivity.EXTRA_POSITION, -1)
                if (position >= 0 ){
                    products[position].name = name
                    products[position].qtt = qtt
                }else {
                    products.add(Product("",name, qtt, false))
                }
                adpapter.notifyDataSetChanged()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listViewProducts.adapter = adpapter

        binding.buttonAddList.setOnClickListener {
            val intent = Intent(this, ProductDetailActivity::class.java)
            resultLauncher.launch(intent)
        }

        val db = Firebase.firestore
        db.collection("shoppingLists")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


    }

    inner class ProductAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return products.size
        }

        override fun getItem(position: Int): Any {
            return products[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = RowProductBinding.inflate(layoutInflater)
            rootView.textViewProduct.text = products[position].name
            rootView.textViewQtt.text = products[position].qtt.toString()
            rootView.checkBox.isChecked = products[position].isChecked
            rootView.root.setOnClickListener {
                val intent = Intent(this@ProductActivity, ProductDetailActivity::class.java)
                intent.putExtra(ProductDetailActivity.EXTRA_NAME, products[position].name)
                intent.putExtra(ProductDetailActivity.EXTRA_QTT, products[position].qtt)
                intent.putExtra(ProductDetailActivity.EXTRA_POSITION, position)
                resultLauncher.launch(intent)
            }
            return rootView.root
        }

    }
}