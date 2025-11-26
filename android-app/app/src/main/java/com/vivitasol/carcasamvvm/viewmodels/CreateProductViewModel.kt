package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.ProductRepository
import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateProductState(
    val name: String = "",
    val designer: String = "",
    val price: String = "", // Usamos String para los campos de texto
    val stock: String = "", // Usamos String para los campos de texto
    val imageUri: Uri? = null
)

class CreateProductViewModel(application: Application, private val repository: ProductRepository) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(CreateProductState())
    val state = _state.asStateFlow()

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun onDesignerChange(designer: String) {
        _state.update { it.copy(designer = designer) }
    }

    fun onPriceChange(price: String) {
        _state.update { it.copy(price = price) }
    }

    fun onStockChange(stock: String) {
        _state.update { it.copy(stock = stock) }
    }

    fun onImageSelected(uri: Uri) {
        _state.update { it.copy(imageUri = uri) }
    }

    fun saveProduct() {
        viewModelScope.launch {
            val currentState = _state.value
            val product = Product(
                name = currentState.name,
                designer = currentState.designer,
                price = currentState.price.toDoubleOrNull() ?: 0.0,
                stock = currentState.stock.toIntOrNull() ?: 0,
                imageUri = currentState.imageUri.toString()
            )
            repository.insert(product)
            // Aquí podrías añadir lógica para navegar hacia atrás o mostrar un mensaje
        }
    }
}
