package com.vivitasol.carcasamvvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class Cliente(
    @PrimaryKey
    val email: String,
    val _id: String? = null, // Para el ID de MongoDB
    val nombre: String,
    val contrasena: String,
    val comuna: String,
    val region: String,
    val rol: String = "usuario",
    val profileImagePath: String? = null
)
