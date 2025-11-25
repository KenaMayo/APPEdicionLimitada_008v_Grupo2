package com.vivitasol.carcasamvvm.navigation

sealed class Route(val route: String) {
    object Login : Route("login")
    object Register : Route("register")
    object Home : Route("home")
    object Detail : Route("detail")
    object CreateProduct : Route("create_product")
    object MenuShell : Route("menu_shell") // Contenedor con drawer
    object UserMenuShell : Route("user_menu_shell") // Contenedor con drawer para usuarios
}
