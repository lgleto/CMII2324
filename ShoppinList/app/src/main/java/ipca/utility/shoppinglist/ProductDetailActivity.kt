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

    var position : Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            val name = it.getString(EXTRA_NAME)
            val qtt = it.getInt(EXTRA_QTT)
            position=it.getInt(EXTRA_POSITION,-1)
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
            intent.putExtra(EXTRA_NAME, binding.editTextProductName.text.toString())
            intent.putExtra(EXTRA_QTT, qtt)
            intent.putExtra(EXTRA_POSITION, position)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_QTT = "extra_qtt"
        const val EXTRA_POSITION = "extra_position"
    }
}