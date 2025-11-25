package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.ProductRepository
import com.vivitasol.carcasamvvm.model.CartItem
import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartState(
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0,
    val showPurchaseSuccess: Boolean = false
)

class CartViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    fun addProduct(product: Product): Boolean {
        val currentState = _state.value
        val existingItem = currentState.items.find { it.product.id == product.id }
        val quantityInCart = existingItem?.quantity ?: 0

        if (quantityInCart >= product.stock) {
            return false // No se puede agregar más, límite de stock alcanzado
        }

        _state.update { state ->
            val newItems = if (existingItem != null) {
                // Incrementar cantidad
                state.items.map {
                    if (it.product.id == product.id) {
                        it.copy(quantity = it.quantity + 1)
                    } else {
                        it
                    }
                }
            } else {
                // Agregar nuevo item
                state.items + CartItem(product = product, quantity = 1)
            }
            val newTotal = newItems.sumOf { it.product.price * it.quantity }
            state.copy(items = newItems, total = newTotal)
        }
        return true
    }

    fun simulatePurchase() {
        viewModelScope.launch {
            _state.value.items.forEach { cartItem ->
                val product = cartItem.product
                val updatedProduct = product.copy(stock = product.stock - cartItem.quantity)
                repository.update(updatedProduct)
            }
            _state.update { it.copy(items = emptyList(), total = 0.0, showPurchaseSuccess = true) }
        }
    }

    fun purchaseMessageShown() {
        _state.update { it.copy(showPurchaseSuccess = false) }
    }
}
