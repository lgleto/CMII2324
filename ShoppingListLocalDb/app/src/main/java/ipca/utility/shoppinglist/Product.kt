package ipca.utility.shoppinglist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query


@Entity
data class Product(
    @PrimaryKey
    var uid: Long,
    var name: String?,
    var qtd: Int,
    var isChecked: Boolean = false)

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun getAll() : List<Product>

    @Query("SELECT * FROM product WHERE uid = :uid")
    fun get(uid : Long) : Product

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(product: Product)

    @Delete
    fun  delete(product: Product)


}

