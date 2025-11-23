package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vivitasol.carcasamvvm.data.ProductRepository

class ViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(CreateProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateProductViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(UserHomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserHomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
