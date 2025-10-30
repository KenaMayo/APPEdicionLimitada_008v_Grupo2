package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.R
import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailState(
    val products: List<Product> = emptyList(),
    val selectedProduct: Product? = null
)

class DetailViewModel : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // Simula la carga de productos agregados recientemente
            _state.update {
                it.copy(
                    products = listOf(
                        Product(1, "Vestido Floral", "Diseñador A", 299.99, 10, R.drawable.servel),
                        Product(3, "Pantalón de Seda", "Diseñador C", 199.99, 15, R.drawable.servel),
                        Product(5, "Falda Plisada", "Diseñador D", 179.99, 12, R.drawable.servel),
                    )
                )
            }
        }
    }

    fun onProductSelected(product: Product) {
        _state.update { it.copy(selectedProduct = product) }
    }

    fun onNameChange(name: String) {
        _state.update {
            it.copy(
                selectedProduct = it.selectedProduct?.copy(name = name)
            )
        }
    }

    fun onDesignerChange(designer: String) {
        _state.update {
            it.copy(
                selectedProduct = it.selectedProduct?.copy(designer = designer)
            )
        }
    }

    fun onPriceChange(price: String) {
        _state.update {
            it.copy(
                selectedProduct = it.selectedProduct?.copy(price = price.toDoubleOrNull() ?: 0.0)
            )
        }
    }

    fun onStockChange(stock: String) {
        _state.update {
            it.copy(
                selectedProduct = it.selectedProduct?.copy(stock = stock.toIntOrNull() ?: 0)
            )
        }
    }

    fun onProductEdited() {
        val editedProduct = _state.value.selectedProduct
        if (editedProduct != null) {
            val currentProducts = _state.value.products.toMutableList()
            val index = currentProducts.indexOfFirst { it.id == editedProduct.id }
            if (index != -1) {
                currentProducts[index] = editedProduct
                _state.update { it.copy(products = currentProducts, selectedProduct = null) }
            } else {
                 _state.update { it.copy(selectedProduct = null) }
            }
        }
    }

     fun clearSelectedProduct() {
        _state.update { it.copy(selectedProduct = null) }
    }
}
