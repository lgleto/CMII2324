package ipca.example.qrcodereader

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ipca.example.qrcodereader.databinding.ActivityMainBinding
import ipca.example.qrcodereader.databinding.DialogQrCodeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class QrCodeDialog  : BottomSheetDialogFragment() {

    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""

    private var _binding: DialogQrCodeBinding? = null
    private val binding get() = _binding!!

    var onResultCallback : ((String?)-> Unit)? = null
    companion object {

        fun newInstance(): QrCodeDialog {
            return QrCodeDialog()
        }

        fun show(fm: FragmentManager,  onResultCallback: (String?)-> Unit) : QrCodeDialog {
            val alertDialog: QrCodeDialog = QrCodeDialog.newInstance()
            alertDialog.show(fm, "fragment_qr_code_dialog")
            alertDialog.onResultCallback = onResultCallback
            return alertDialog
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogQrCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomDialogFragment)
        dialog.setCancelable(false)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.buttonCancel.setOnClickListener {
            onResultCallback?.invoke(null)
            dismiss()
        }

        val aniSlide: Animation =
            AnimationUtils.loadAnimation(requireContext(),
                R.anim.scanner_animation)
        binding.barcodeLine.startAnimation(aniSlide)
        setupControls()

    }

    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build()
        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        binding.cameraSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {

            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(requireContext(), "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue

                    lifecycleScope.launch (Dispatchers.Main){
                        cameraSource.stop()
                        onResultCallback?.invoke(scannedValue)
                        dismiss()
                    }

                }else{
                    Toast.makeText(requireContext(), "Sem resultados", Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.stop()
    }
}