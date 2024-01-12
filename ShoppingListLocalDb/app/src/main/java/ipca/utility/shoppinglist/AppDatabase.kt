package ipca.utility.shoppinglist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao() : ProductDao



    companion object{

        @Volatile
        private var INSTANCE : AppDatabase? =  null

        fun getInstance(context: Context) : AppDatabase? {

            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java, "db-shopping_list"
                    ).fallbackToDestructiveMigration().build()
                }
            }

            return INSTANCE
        }
    }

}