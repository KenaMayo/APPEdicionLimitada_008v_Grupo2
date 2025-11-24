package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.ProductRepository
import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartState(
    val products: List<Product> = emptyList(),
    val total: Double = 0.0,
    val showPurchaseSuccess: Boolean = false
)

class CartViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    fun addProduct(product: Product) {
        _state.update {
            val newProducts = it.products + product
            val newTotal = newProducts.sumOf { p -> p.price }
            it.copy(products = newProducts, total = newTotal)
        }
    }

    fun simulatePurchase() {
        viewModelScope.launch {
            _state.value.products.forEach { product ->
                val updatedProduct = product.copy(stock = product.stock - 1)
                repository.update(updatedProduct)
            }
            _state.update { it.copy(products = emptyList(), total = 0.0, showPurchaseSuccess = true) }
        }
    }

    fun purchaseMessageShown() {
        _state.update { it.copy(showPurchaseSuccess = false) }
    }
}
