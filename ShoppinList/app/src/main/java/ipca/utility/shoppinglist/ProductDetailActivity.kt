package ipca.utility.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ipca.utility.shoppinglist.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductDetailBinding

    var qtt : Int
        get() = binding.textViewQtt.text.toString().toInt()
        set(value){
            if (value >= 0) {
                binding.textViewQtt.text = value.toString()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            val name = it.getString("extra_name")
            val qtt = it.getInt("extra_qtt")
            binding.editTextProductName.setText ( name)
            this@ProductDetailActivity.qtt = qtt
        }

        binding.buttonIncrement.setOnClickListener {
            qtt++
        }
        binding.buttonDecrement.setOnClickListener {
            qtt--
        }
        binding.buttonDone.setOnClickListener {
            val intent = Intent()
            intent.putExtra("extra_name",binding.editTextProductName.text.toString())
            intent.putExtra("extra_qtt",qtt)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}