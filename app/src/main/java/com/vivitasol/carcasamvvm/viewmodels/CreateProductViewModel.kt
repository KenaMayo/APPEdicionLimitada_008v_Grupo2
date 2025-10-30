package com.vivitasol.carcasamvvm.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CreateProductState(
    val name: String = "",
    val designer: String = "",
    val price: String = "",
    val stock: String = "",
    val imageUri: Uri? = null
)

data class CreateProductErrors(
    val name: String? = null,
    val designer: String? = null,
    val price: String? = null,
    val stock: String? = null,
    val image: String? = null
)

class CreateProductViewModel : ViewModel() {
    private val _state = MutableStateFlow(CreateProductState())
    val state = _state.asStateFlow()

    private val _errors = MutableStateFlow(CreateProductErrors())
    val errors = _errors.asStateFlow()

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
        _errors.update { it.copy(name = null) }
    }

    fun onDesignerChange(designer: String) {
        _state.update { it.copy(designer = designer) }
        _errors.update { it.copy(designer = null) }
    }

    fun onPriceChange(price: String) {
        _state.update { it.copy(price = price) }
        _errors.update { it.copy(price = null) }
    }

    fun onStockChange(stock: String) {
        _state.update { it.copy(stock = stock) }
        _errors.update { it.copy(stock = null) }
    }

    fun onImageChange(uri: Uri?) {
        _state.update { it.copy(imageUri = uri) }
        _errors.update { it.copy(image = null) }
    }

    fun validate(): Boolean {
        val currentState = _state.value
        val nameError = if (currentState.name.isBlank()) "El nombre es obligatorio" else null
        val designerError = if (currentState.designer.isBlank()) "El diseñador es obligatorio" else null
        val priceError = if (currentState.price.isBlank()) "El precio es obligatorio" else null
        val stockError = if (currentState.stock.isBlank()) "El stock es obligatorio" else null
        val imageError = if (currentState.imageUri == null) "La imagen es obligatoria" else null

        _errors.update { it.copy(
            name = nameError,
            designer = designerError,
            price = priceError,
            stock = stockError,
            image = imageError
        ) }

        return nameError == null && designerError == null && priceError == null && stockError == null && imageError == null
    }

    fun reset() {
        _state.value = CreateProductState()
        _errors.value = CreateProductErrors()
    }
}
