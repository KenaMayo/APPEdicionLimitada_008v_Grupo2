package com.vivitasol.carcasamvvm.model

import com.google.gson.annotations.SerializedName

data class Cliente(
    @SerializedName("_id")
    val id: String,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val comuna: String,
    val region: String
)
