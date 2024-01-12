package ipca.utility.shoppinglist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import ipca.utility.shoppinglist.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    var qtd : Int
        get()  = binding.textViewQtd.text.toString().toInt()
        set(value){
            if (value >= 0){
                binding.textViewQtd.text = value.toString()
            }
        }

    private lateinit var binding : ActivityProductDetailBinding

    var position : Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            position = it.getInt(DATA_POSITION, -1)
            qtd = it.getInt(DATA_QTD)
            val name = it.getString(DATA_NAME)
            binding.editTextProductName.setText(name)

        }

        binding.buttonIncrement.setOnClickListener {
            qtd += 1
        }

        binding.buttonDecrement.setOnClickListener {
            qtd -= 1
        }

        binding.buttonDone.setOnClickListener {
            val intent = Intent()
            intent.putExtra(DATA_QTD, qtd)
            intent.putExtra(DATA_NAME , binding.editTextProductName.text.toString())
            intent.putExtra(DATA_POSITION ,position)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    companion object {
        const val DATA_NAME = "data_name"
        const val DATA_QTD  = "data_qtd"
        const val DATA_POSITION = "data_position"
    }


}