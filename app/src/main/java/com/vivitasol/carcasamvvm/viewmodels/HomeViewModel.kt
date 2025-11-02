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
            Product(1, "Chaqueta Jeans", "Diseñador A", 299.99, 10, R.drawable.chaquetajeans),
            Product(2, "Pendientes Rubi", "Diseñador B", 499.99, 5, R.drawable.aros),
            Product(3, "Blusa de Seda", "Diseñador C", 199.99, 15, R.drawable.blusa),
            Product(4, "Chaqueta Unisex", "Diseñador A", 149.99, 20, R.drawable.chaquetaunisex),
            Product(5, "Poleron con Capucha", "Diseñador D", 179.99, 12, R.drawable.poleroncapucha),
            Product(6, "Sweter de Hilo", "Diseñador B", 699.99, 8, R.drawable.sweter),
            Product(7, "Boina", "Diseñador C", 129.99, 25, R.drawable.boina),
            Product(8, "Top de Cuerina", "Diseñador D", 349.99, 7, R.drawable.top)
        )
    }
}
