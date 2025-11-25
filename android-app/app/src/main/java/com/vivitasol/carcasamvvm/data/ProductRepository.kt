package com.vivitasol.carcasamvvm.data

import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productoDao: ProductoDao) {

    val allProducts: Flow<List<Product>> = productoDao.getAll()

    suspend fun insert(product: Product) {
        productoDao.insert(product)
    }

    suspend fun update(product: Product) {
        productoDao.update(product)
    }

    suspend fun delete(product: Product) {
        productoDao.delete(product)
    }

    suspend fun count(): Int {
        return productoDao.count()
    }

    suspend fun deleteAll() {
        productoDao.deleteAll()
    }
}
