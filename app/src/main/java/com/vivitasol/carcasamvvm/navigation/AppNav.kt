package com.vivitasol.carcasamvvm.navigation

sealed class Route(val route: String) {
    object Login : Route("login")
    object Home : Route("home")
    object Detail : Route("detail")
    object CreateProduct : Route("create_product")
    object MenuShell : Route("menu_shell") // Contenedor con drawer
}
