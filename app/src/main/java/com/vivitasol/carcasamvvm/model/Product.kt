package com.vivitasol.carcasamvvm.model

import androidx.annotation.DrawableRes

data class Product(
    val id: Int,
    val name: String,
    val designer: String,
    val price: Double,
    val stock: Int,
    @DrawableRes val imageRes: Int
)
