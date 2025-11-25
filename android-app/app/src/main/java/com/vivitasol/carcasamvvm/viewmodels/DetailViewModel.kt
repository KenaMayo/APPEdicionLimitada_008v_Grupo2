package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.ProductRepository
import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailState(
    val selectedProduct: Product? = null
)

class DetailViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onProductSelected(product: Product) {
        _state.update { it.copy(selectedProduct = product) }
    }

    fun onNameChange(name: String) {
        _state.update {
            it.copy(selectedProduct = it.selectedProduct?.copy(name = name))
        }
    }

    fun onDesignerChange(designer: String) {
        _state.update {
            it.copy(selectedProduct = it.selectedProduct?.copy(designer = designer))
        }
    }

    fun onPriceChange(price: String) {
        _state.update {
            it.copy(selectedProduct = it.selectedProduct?.copy(price = price.toDoubleOrNull() ?: 0.0))
        }
    }

    fun onStockChange(stock: String) {
        _state.update {
            it.copy(selectedProduct = it.selectedProduct?.copy(stock = stock.toIntOrNull() ?: 0))
        }
    }

    fun onProductEdited() {
        viewModelScope.launch {
            _state.value.selectedProduct?.let {
                repository.update(it)
                clearSelectedProduct()
            }
        }
    }

    fun deleteProduct() {
        viewModelScope.launch {
            _state.value.selectedProduct?.let {
                repository.delete(it)
                clearSelectedProduct()
            }
        }
    }

    fun clearSelectedProduct() {
        _state.update { it.copy(selectedProduct = null) }
    }
}
