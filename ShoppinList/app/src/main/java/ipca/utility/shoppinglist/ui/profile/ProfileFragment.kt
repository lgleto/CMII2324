package ipca.utility.shoppinglist.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.utility.shoppinglist.R
import ipca.utility.shoppinglist.TAG
import ipca.utility.shoppinglist.databinding.FragmentProductBinding
import ipca.utility.shoppinglist.databinding.FragmentProfileBinding
import ipca.utility.shoppinglist.model.User

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonSave.setOnClickListener {
            var auth = Firebase.auth
            val userId = auth.currentUser!!.uid

            val user = User(
                "",
                binding.editTextName.text.toString(),
                auth?.currentUser?.email,
                null
            )


            val db = Firebase.firestore
            db.collection("users")
                .document(userId)
                .set(user.toHasMap())
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(requireContext(), "User saved", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(requireContext(), "No internet!", Toast.LENGTH_LONG).show()
                }
        }
    }

}