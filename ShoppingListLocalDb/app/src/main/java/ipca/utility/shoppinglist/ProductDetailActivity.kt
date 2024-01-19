package ipca.utility.shoppinglist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.lifecycle.lifecycleScope
import ipca.utility.shoppinglist.databinding.ActivityProductDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class ProductDetailActivity : AppCompatActivity() {

    var qtd : Int
        get()  = binding.textViewQtd.text.toString().toInt()
        set(value){
            if (value >= 0){
                binding.textViewQtd.text = value.toString()
            }
        }

    private lateinit var binding : ActivityProductDetailBinding

    var product : Product? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            val uid = it.getLong(PRODUCT_ID, -1)
            lifecycleScope.launch (Dispatchers.IO){
                product = AppDatabase
                    .getInstance(this@ProductDetailActivity)
                    ?.productDao()
                    ?.get(uid)
                lifecycleScope.launch (Dispatchers.Main){
                    product?.let {
                        qtd = it.qtd
                        binding.editTextProductName.setText(it.name)
                    }
                }
            }
        }

        binding.buttonIncrement.setOnClickListener {
            qtd += 1
        }

        binding.buttonDecrement.setOnClickListener {
            qtd -= 1
        }

        binding.buttonDone.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                AppDatabase.getInstance(this@ProductDetailActivity)?.productDao()?.add(
                    Product(
                        if (product==null) System.currentTimeMillis() else product!!.uid,
                        binding.editTextProductName.text.toString(),
                        qtd,
                        product?.isChecked?:false
                    )
                )
                lifecycleScope.launch(Dispatchers.Main) {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }

    }

    companion object {
        const val PRODUCT_ID = "product_id"
    }


}