package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import com.vivitasol.carcasamvvm.R
import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    init {
        _products.value = listOf(
            Product(1, "Vestido Floral", "Diseñador A", 299.99, 10, R.drawable.servel),
            Product(2, "Chaqueta de Cuero", "Diseñador B", 499.99, 5, R.drawable.servel),
            Product(3, "Pantalón de Seda", "Diseñador C", 199.99, 15, R.drawable.servel),
            Product(4, "Blusa de Encaje", "Diseñador A", 149.99, 20, R.drawable.servel),
            Product(5, "Falda Plisada", "Diseñador D", 179.99, 12, R.drawable.servel),
            Product(6, "Abrigo de Lana", "Diseñador B", 699.99, 8, R.drawable.servel),
            Product(7, "Camisa de Lino", "Diseñador C", 129.99, 25, R.drawable.servel),
            Product(8, "Bolso de Piel", "Diseñador D", 349.99, 7, R.drawable.servel)
        )
    }
}
