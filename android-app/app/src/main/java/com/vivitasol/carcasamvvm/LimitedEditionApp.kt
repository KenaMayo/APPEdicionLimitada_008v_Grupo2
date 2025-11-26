package com.vivitasol.carcasamvvm

import android.app.Application
import com.vivitasol.carcasamvvm.data.AppDatabase
import com.vivitasol.carcasamvvm.data.ClientRepository
import com.vivitasol.carcasamvvm.data.ProductRepository

class LimitedEditionApp : Application() {
    // Usamos 'lazy' para que la base de datos y el repositorio
    // solo se creen cuando se necesiten por primera vez.
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ProductRepository(database.productoDao()) }
    val clientRepository by lazy { ClientRepository() } // Añadido de nuevo
}
