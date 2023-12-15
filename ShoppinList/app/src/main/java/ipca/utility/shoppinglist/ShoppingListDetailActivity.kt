package ipca.utility.shoppinglist

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ipca.utility.shoppinglist.databinding.ActivityShoppongListDetailBinding
import ipca.utility.shoppinglist.model.ShoppingList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class ShoppingListDetailActivity : AppCompatActivity() {

    val db = Firebase.firestore

    private lateinit var binding: ActivityShoppongListDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppongListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonDone.setOnClickListener {
            addShoppingList(ShoppingList("",
                binding.editTextListName.text.toString(),
                null
            ))
        }

        binding.buttonTakePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }

    fun addShoppingList(shoppingList: ShoppingList){

        val storage = Firebase.storage
        val storageRef = storage.reference

        var file = Uri.fromFile(File(currentPhotoPath))
        val photoRef = storageRef.child("images/${file.lastPathSegment}")
        val uploadTask = photoRef.putFile(file)

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.e(TAG, it.localizedMessage)

        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...

            shoppingList.pathToImage = "images/${file.lastPathSegment}"

            val data = shoppingList.toHasMap()

            db.collection("shoppingLists")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                    Toast.makeText(this, "List added", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(this, "No internet!", Toast.LENGTH_LONG).show()
                }
        }



    }

    // take photos

    val REQUEST_IMAGE_CAPTURE = 1


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            BitmapFactory.decodeFile(currentPhotoPath)?.also { bitmap ->
                binding.imageViewPhoto.setImageBitmap(bitmap)
            }
        }
    }


    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = UUID.randomUUID().toString()
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(this@ShoppingListDetailActivity.packageManager).also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this@ShoppingListDetailActivity,
                        "ipca.utility.shoppinglist.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

}