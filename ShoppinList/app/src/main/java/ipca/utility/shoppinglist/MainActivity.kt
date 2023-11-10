package ipca.utility.shoppinglist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import ipca.utility.shoppinglist.databinding.ActivityMainBinding
import ipca.utility.shoppinglist.databinding.RowProductBinding
import ipca.utility.shoppinglist.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    var products = arrayListOf<Product>()

    private lateinit var binding: ActivityMainBinding
    private  var  adpapter = ProductAdapter()
    val resultLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            it.data?.let { data ->
                val name = data.getStringExtra("extra_name")?:""
                val qtt = data.getIntExtra("extra_qtt",0)
                val position = data.getIntExtra("extra_position", -1)
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

        binding.buttonAddProduct.setOnClickListener {
            val intent = Intent(this, ProductDetailActivity::class.java)
            resultLauncher.launch(intent)
        }

        Backend.fetchProducts(lifecycleScope) {
            products = it
            adpapter.notifyDataSetChanged()
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
                val intent = Intent(this@MainActivity, ProductDetailActivity::class.java)
                intent.putExtra("extra_name", products[position].name)
                intent.putExtra("extra_qtt", products[position].qtt)
                intent.putExtra("extra_position", position)
                resultLauncher.launch(intent)
            }
            return rootView.root
        }

    }
}