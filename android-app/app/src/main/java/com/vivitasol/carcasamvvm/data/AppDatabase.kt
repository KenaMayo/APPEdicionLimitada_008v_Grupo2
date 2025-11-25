package com.vivitasol.carcasamvvm.data

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vivitasol.carcasamvvm.R
import com.vivitasol.carcasamvvm.model.Cliente
import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Product::class, Cliente::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun clienteDao(): ClienteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    prepopulate(context, database.productoDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                // Pre-populate database after instance is created
                instance.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        
                        if (database.productoDao().count() == 0) {
                             prepopulate(context, database.productoDao())
                        }
                    }
                }
                instance
            }
        }

        private suspend fun prepopulate(context: Context, productDao: ProductoDao) {
            val vestidoImageUri = resourceToUri(context, R.drawable.vestidoflor).toString()
            val chaquetaImageUri = resourceToUri(context, R.drawable.chaquetajeans).toString()
            val pantalonImageUri = resourceToUri(context, R.drawable.chaquetaunisex).toString()
            val blusaImageUri = resourceToUri(context, R.drawable.blusaencaje).toString()
            val faldaImageUri = resourceToUri(context, R.drawable.falda).toString()
            val abrigoImageUri = resourceToUri(context, R.drawable.chaquetalana).toString()
            val camisaImageUri = resourceToUri(context, R.drawable.blusa).toString()
            val bolsoImageUri = resourceToUri(context, R.drawable.bolsopiel).toString()

            productDao.insert(Product(name = "Vestido Floral", designer = "Diseñador A", price = 45990.0, stock = 10, imageUri = vestidoImageUri))
            productDao.insert(Product(name = "Chaqueta de Cuero", designer = "Diseñador B", price = 89990.0, stock = 5, imageUri = chaquetaImageUri))
            productDao.insert(Product(name = "Pantalón de Seda", designer = "Diseñador C", price = 59990.0, stock = 15, imageUri = pantalonImageUri))
            productDao.insert(Product(name = "Blusa de Encaje", designer = "Diseñador A", price = 39990.0, stock = 20, imageUri = blusaImageUri))
            productDao.insert(Product(name = "Falda Plisada", designer = "Diseñador D", price = 49990.0, stock = 12, imageUri = faldaImageUri))
            productDao.insert(Product(name = "Abrigo de Lana", designer = "Diseñador B", price = 99990.0, stock = 8, imageUri = abrigoImageUri))
            productDao.insert(Product(name = "Camisa de Lino", designer = "Diseñador C", price = 42990.0, stock = 25, imageUri = camisaImageUri))
            productDao.insert(Product(name = "Bolso de Piel", designer = "Diseñador D", price = 79990.0, stock = 7, imageUri = bolsoImageUri))
        }

        private fun resourceToUri(context: Context, resourceId: Int): Uri {
            return Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(context.resources.getResourcePackageName(resourceId))
                .appendPath(context.resources.getResourceTypeName(resourceId))
                .appendPath(context.resources.getResourceEntryName(resourceId))
                .build()
        }
    }
}