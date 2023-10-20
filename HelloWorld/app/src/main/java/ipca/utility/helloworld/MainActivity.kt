package ipca.utility.helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this)
        textView.text = "Hello world!"
        textView.textSize = 54.0f
        val button = Button(this)


        val linearLayout = LinearLayout(this)
        linearLayout.addView(textView)
        linearLayout.addView(button)

        setContentView(linearLayout)


        button.text = "Traduzir"
        button.setOnClickListener {
            textView.text = "ola mundo"
        }

    }
}
