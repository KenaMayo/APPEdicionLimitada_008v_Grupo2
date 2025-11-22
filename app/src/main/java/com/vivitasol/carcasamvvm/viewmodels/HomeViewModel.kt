package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.R
import com.vivitasol.carcasamvvm.data.ProductRepository
import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductRepository) : ViewModel() {

    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        // Si la base de datos está vacía, la llenamos con datos de ejemplo.
        viewModelScope.launch {
            if (repository.count() == 0) {
                repository.insert(Product(name = "Vestido Floral", designer = "Diseñador A", price = 299.99, stock = 10, imageRes = R.drawable.servel))
                repository.insert(Product(name = "Chaqueta de Cuero", designer = "Diseñador B", price = 499.99, stock = 5, imageRes = R.drawable.servel))
                repository.insert(Product(name = "Pantalón de Seda", designer = "Diseñador C", price = 199.99, stock = 15, imageRes = R.drawable.servel))
                repository.insert(Product(name = "Blusa de Encaje", designer = "Diseñador A", price = 149.99, stock = 20, imageRes = R.drawable.servel))
                repository.insert(Product(name = "Falda Plisada", designer = "Diseñador D", price = 179.99, stock = 12, imageRes = R.drawable.servel))
                repository.insert(Product(name = "Abrigo de Lana", designer = "Diseñador B", price = 699.99, stock = 8, imageRes = R.drawable.servel))
                repository.insert(Product(name = "Camisa de Lino", designer = "Diseñador C", price = 129.99, stock = 25, imageRes = R.drawable.servel))
                repository.insert(Product(name = "Bolso de Piel", designer = "Diseñador D", price = 349.99, stock = 7, imageRes = R.drawable.servel))
            }
        }
    }
}
