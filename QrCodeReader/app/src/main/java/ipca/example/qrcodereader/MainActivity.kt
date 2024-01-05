package ipca.example.qrcodereader

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import ipca.example.qrcodereader.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonScan.isEnabled = false

        if (ContextCompat.checkSelfPermission(
                this@MainActivity, android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            binding.buttonScan.isEnabled = true
        }

        binding.buttonScan.setOnClickListener {
            QrCodeDialog.show(supportFragmentManager){
                binding.textView.text = it?:"Sem resultados"
            }
        }

    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.buttonScan.isEnabled = true
            } else {
                Toast.makeText(applicationContext, "Não deu permissão para usar a camâra.", Toast.LENGTH_SHORT).show()
            }
        }
    }




}