package ipca.example.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var isMuted = false
    var lastValueL = 0
    var lastValueR = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val verticalSliderViewL = findViewById<VerticalSliderView>(R.id.verticalSliderViewL)
        val verticalSliderViewR = findViewById<VerticalSliderView>(R.id.verticalSliderViewR)
        val textViewL = findViewById<TextView>(R.id.textViewL)
        val textViewR = findViewById<TextView>(R.id.textViewR)
        val buttonMute = findViewById<Button>(R.id.buttonMute)

        verticalSliderViewL.setOnValueChanged{
            textViewL.text = it.toString()
            lastValueL = it
        }

        verticalSliderViewR.setOnValueChanged {
            textViewR.text = it.toString()
            lastValueR = it
        }

        buttonMute.setOnClickListener {
            isMuted = !isMuted
            if (isMuted) {
                buttonMute.text = "UnMute"
                verticalSliderViewL.percentage = 0
                verticalSliderViewR.percentage = 0
                textViewL.text = 0.toString()
                textViewR.text = 0.toString()
            }
            else {
                buttonMute.text = "Mute"
                verticalSliderViewL.percentage = lastValueL
                verticalSliderViewR.percentage = lastValueR
                textViewL.text = lastValueL.toString()
                textViewR.text = lastValueR.toString()
            }
        }


    }
}