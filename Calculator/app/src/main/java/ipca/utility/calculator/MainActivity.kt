package ipca.utility.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import ipca.utility.calculator.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    var userIsInTheMiddleOfIntroduction = true
    var calculatorBrain = CalculatorBrain()

    var displayValue : Double
        get() = binding.textViewDisplay.text.toString().toDouble()
        set(newValue) {
            if (newValue % 1 == 0.0)  {
                binding.textViewDisplay.text = newValue.toInt().toString()
            }else{
                binding.textViewDisplay.text = newValue.toString()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater )
        setContentView(binding.root)

        binding.buttonNum1.setOnClickListener(onButtonNumTouched)
        binding.buttonNum2.setOnClickListener(onButtonNumTouched)
        binding.buttonNum3.setOnClickListener(onButtonNumTouched)
        binding.buttonNum4.setOnClickListener(onButtonNumTouched)
        binding.buttonNum5.setOnClickListener(onButtonNumTouched)
        binding.buttonNum6.setOnClickListener(onButtonNumTouched)
        binding.buttonNum7.setOnClickListener(onButtonNumTouched)
        binding.buttonNum8.setOnClickListener(onButtonNumTouched)
        binding.buttonNum9.setOnClickListener(onButtonNumTouched)
        binding.buttonNum0.setOnClickListener(onButtonNumTouched)
        binding.buttonDot .setOnClickListener(onButtonNumTouched)

        binding.buttonAdd       .setOnClickListener (onButtonOperation)
        binding.buttonSubtract  .setOnClickListener (onButtonOperation)
        binding.buttonDivide    .setOnClickListener (onButtonOperation)
        binding.buttonMultiply  .setOnClickListener (onButtonOperation)

        binding.buttonEqual.setOnClickListener {
            displayValue = calculatorBrain.doOperation(displayValue)
            userIsInTheMiddleOfIntroduction = false
        }

        binding.buttonAC.setOnClickListener {
            displayValue = 0.0
            calculatorBrain.operand = 0.0
            calculatorBrain.operator = null
        }
    }

    override fun onResume() {
        super.onResume()

    }



    var onButtonOperation : (View)->Unit = {

        calculatorBrain.operator?.let {op->
            displayValue = calculatorBrain.doOperation(displayValue)
        }
        when((it as Button).text){
            "+" -> calculatorBrain.operator = Operator.ADD
            "-" -> calculatorBrain.operator = Operator.SUBTRACT
            "*" -> calculatorBrain.operator = Operator.MULTIPLY
            "/" -> calculatorBrain.operator = Operator.DIVIDE
        }
        calculatorBrain.operand = displayValue
        userIsInTheMiddleOfIntroduction = false
    }

    var onButtonNumTouched : (View)->Unit = {
        val buttonTxt = (it as Button).text
        if (userIsInTheMiddleOfIntroduction) {
            if (buttonTxt == ".") {
                if (!binding.textViewDisplay.text.contains(".")) {
                    binding.textViewDisplay.append(buttonTxt)
                }
            } else {
                if (binding.textViewDisplay.text == "0") {
                    binding.textViewDisplay.text = buttonTxt
                } else {
                    binding.textViewDisplay.append(buttonTxt)
                }
            }
        }else{
            if (buttonTxt == ".") {
                binding.textViewDisplay.text = "0."
            }else {
                binding.textViewDisplay.text = buttonTxt
            }
        }
        userIsInTheMiddleOfIntroduction = true
    }





}