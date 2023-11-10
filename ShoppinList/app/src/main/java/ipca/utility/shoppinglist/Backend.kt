package ipca.utility.shoppinglist

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import ipca.utility.shoppinglist.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object Backend {

    fun fetchProducts(lifecycleScope : LifecycleCoroutineScope,
                      onCompletion:(ArrayList<Product>)->Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()

                .url("http://192.168.56.149:7105/Product")
                .build()

            client.newCall(request).execute().use { response ->
                val result = response.body!!.string()
                val products = arrayListOf<Product>()
                val jsonArray = JSONArray(result)
                for ( index in 0..<jsonArray.length()){
                    val jsonObject = jsonArray[index] as JSONObject

                    products.add(Product.fromJson(jsonObject))
                }
                lifecycleScope.launch(Dispatchers.Main) {
                    onCompletion.invoke(products)
                }
            }
        }
    }
}