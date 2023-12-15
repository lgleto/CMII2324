package ipca.utility.shoppinglist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ipca.utility.shoppinglist.databinding.FragmentProductDetailBinding

class ProductDetailFragment : Fragment() {

    var qtt : Int
        get() = binding.textViewQtt.text.toString().toInt()
        set(value){
            if (value >= 0) {
                binding.textViewQtt.text = value.toString()
            }
        }

    var position : Int = -1

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*
        intent.extras?.let {
            val name = it.getString(EXTRA_NAME)
            val qtt = it.getInt(EXTRA_QTT)
            position=it.getInt(EXTRA_POSITION,-1)
            binding.editTextProductName.setText ( name)
            this@ProductDetailFragment.qtt = qtt
        }
*/
        binding.buttonIncrement.setOnClickListener {
            qtt++
        }
        binding.buttonDecrement.setOnClickListener {
            qtt--
        }
        binding.buttonDone.setOnClickListener {
            //val intent = Intent()
            //intent.putExtra(EXTRA_NAME, binding.editTextProductName.text.toString())
            //intent.putExtra(EXTRA_QTT, qtt)
            //intent.putExtra(EXTRA_POSITION, position)
            //setResult(RESULT_OK, intent)
            //finish()
        }

    }

    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_QTT = "extra_qtt"
        const val EXTRA_POSITION = "extra_position"
    }
}