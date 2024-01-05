package ipca.utility.shoppinglist.ui.home

import android.app.SearchManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ipca.utility.shoppinglist.R
import ipca.utility.shoppinglist.databinding.FragmentHomeBinding
import ipca.utility.shoppinglist.databinding.RowListBinding
import ipca.utility.shoppinglist.model.ShoppingList
import ipca.utility.shoppinglist.removeAccentsLowerCase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    var shoppingLists = arrayListOf<ShoppingList>()
    private  var  adpapter = ShoppingListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listViewProducts.adapter = adpapter

        binding.buttonAddList.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_shoppingListDetailFragment)
        }

        val db = Firebase.firestore
        db.collection("shoppingLists")
            .get()
            .addOnSuccessListener  { snapshoot ->
                snapshoot?.documents?.let {
                    this.shoppingLists.clear()
                    for (document in it) {
                        document.data?.let{ data ->
                            this.shoppingLists.add(
                                ShoppingList.fromSnapshot(
                                    document.id,
                                    data
                                )
                            )
                        }
                    }
                    this.adpapter.notifyDataSetChanged()
                }


            }
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private var mScanning = false

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        if (!mScanning) {
            menu.findItem(R.id.menu_refresh).actionView = null
        } else {
            menu.findItem(R.id.menu_refresh).setActionView(
                R.layout.actionbar_indeterminate_progress)
        }

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager?
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
        searchView.setSearchableInfo(
            searchManager!!.getSearchableInfo(requireActivity().componentName)
        )

        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    searchList(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    searchList(query)
                    return true
                }
            }
        searchView.setOnQueryTextListener(queryTextListener)
    }

    fun searchList( search: String) {
        val db = Firebase.firestore
        db.collection("shoppingLists")
            .whereGreaterThanOrEqualTo("name_search", search.removeAccentsLowerCase())
            .whereLessThanOrEqualTo("name_search", search.removeAccentsLowerCase() + '\uf8ff')

            .get()
            .addOnSuccessListener  { snapshoot ->
                snapshoot?.documents?.let {
                    this.shoppingLists.clear()
                    for (document in it) {
                        document.data?.let{ data ->
                            this.shoppingLists.add(
                                ShoppingList.fromSnapshot(
                                    document.id,
                                    data
                                )
                            )
                        }
                    }
                    this.adpapter.notifyDataSetChanged()
                }


            }
    }

    inner class ShoppingListAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return shoppingLists.size
        }

        override fun getItem(position: Int): Any {
            return shoppingLists[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = RowListBinding.inflate(layoutInflater)
            rootView.textViewListName.text = shoppingLists[position].name

            shoppingLists[position].pathToImage?.let {
                val storage = Firebase.storage
                val storageRef = storage.reference
                val pathReference = storageRef.child(it)
                val ONE_MEGABYTE: Long = 10 * 1024 * 1024
                pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { data ->
                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.count())
                    rootView.imageViewPhotoList.setImageBitmap(bitmap)
                }.addOnFailureListener {
                    // Handle any errors
                }

            }

            rootView.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("listId", shoppingLists[position].id)
                findNavController().navigate(R.id.action_navigation_home_to_productFragment, bundle)
            }


            return rootView.root
        }
    }
}