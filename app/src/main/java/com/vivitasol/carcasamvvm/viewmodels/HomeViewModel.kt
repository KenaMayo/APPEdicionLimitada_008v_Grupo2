package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.R
import com.vivitasol.carcasamvvm.data.ProductRepository
import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application, private val repository: ProductRepository) : AndroidViewModel(application) {

    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        // Si la base de datos está vacía, la llenamos con datos de ejemplo.
        viewModelScope.launch {
            if (repository.count() == 0) {
                val imageUri = resourceToUri(R.drawable.servel)
                repository.insert(Product(name = "Vestido Floral", designer = "Diseñador A", price = 45990.0, stock = 10, imageUri = imageUri.toString()))
                repository.insert(Product(name = "Chaqueta de Cuero", designer = "Diseñador B", price = 89990.0, stock = 5, imageUri = imageUri.toString()))
                repository.insert(Product(name = "Pantalón de Seda", designer = "Diseñador C", price = 59990.0, stock = 15, imageUri = imageUri.toString()))
                repository.insert(Product(name = "Blusa de Encaje", designer = "Diseñador A", price = 39990.0, stock = 20, imageUri = imageUri.toString()))
                repository.insert(Product(name = "Falda Plisada", designer = "Diseñador D", price = 49990.0, stock = 12, imageUri = imageUri.toString()))
                repository.insert(Product(name = "Abrigo de Lana", designer = "Diseñador B", price = 99990.0, stock = 8, imageUri = imageUri.toString()))
                repository.insert(Product(name = "Camisa de Lino", designer = "Diseñador C", price = 42990.0, stock = 25, imageUri = imageUri.toString()))
                repository.insert(Product(name = "Bolso de Piel", designer = "Diseñador D", price = 79990.0, stock = 7, imageUri = imageUri.toString()))
            }
        }
    }

    private fun resourceToUri(resourceId: Int): Uri {
        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(getApplication<Application>().resources.getResourcePackageName(resourceId))
            .appendPath(getApplication<Application>().resources.getResourceTypeName(resourceId))
            .appendPath(getApplication<Application>().resources.getResourceEntryName(resourceId))
            .build()
    }
}
