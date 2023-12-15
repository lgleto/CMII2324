package ipca.utility.shoppinglist.ui.home

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.utility.shoppinglist.TAG
import ipca.utility.shoppinglist.databinding.FragmentProductBinding
import ipca.utility.shoppinglist.databinding.RowProductBinding
import ipca.utility.shoppinglist.model.Product


class ProductFragment : Fragment() {
    var products = arrayListOf<Product>()
    private  var  adpapter = ProductAdapter()
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private var listId : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val resultLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            it.data?.let { data ->
                val name = data.getStringExtra(ProductDetailFragment.EXTRA_NAME)?:""
                val qtt = data.getIntExtra(ProductDetailFragment.EXTRA_QTT,0)
                val position = data.getIntExtra(ProductDetailFragment.EXTRA_POSITION, -1)
                if (position >= 0 ){
                    products[position].name = name
                    products[position].qtt = qtt.toLong()
                }else {
                    products.add(Product("",name, qtt.toLong(), false))
                }
                adpapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listId = arguments?.getString("listId")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listViewProducts.adapter = adpapter

        binding.buttonAddList.setOnClickListener {
            //val intent = Intent(this, ProductDetailFragment::class.java)
            //resultLauncher.launch(intent)
        }

        val db = Firebase.firestore
        db.collection("shoppingLists")
            .document(listId!!)
            .collection("products")
            .addSnapshotListener { value, error ->
                value?.documents?.let {
                    products.clear()
                    for (document in it) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        val product = Product.fromSnapshot(document.id, document?.data!!)
                        products.add(product)
                    }
                    adpapter.notifyDataSetChanged()
                }

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
                //val intent = Intent(this@ProductFragment, ProductDetailFragment::class.java)
                //intent.putExtra(ProductDetailFragment.EXTRA_NAME, products[position].name)
                //intent.putExtra(ProductDetailFragment.EXTRA_QTT, products[position].qtt)
                //intent.putExtra(ProductDetailFragment.EXTRA_POSITION, position)
                //resultLauncher.launch(intent)
            }
            return rootView.root
        }

    }
}