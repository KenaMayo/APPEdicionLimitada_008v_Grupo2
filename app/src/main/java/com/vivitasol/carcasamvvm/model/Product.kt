package com.vivitasol.carcasamvvm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "designer")
    val designer: String,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "stock")
    val stock: Int,

    // En una app real, aquí guardarías una URL o una ruta de archivo.
    // Para este ejemplo, mantenemos el ID del recurso drawable.
    @ColumnInfo(name = "image_res")
    val imageRes: Int
)
