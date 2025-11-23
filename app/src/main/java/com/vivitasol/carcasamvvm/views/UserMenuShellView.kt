package com.vivitasol.carcasamvvm.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vivitasol.carcasamvvm.data.PrefsRepo
import com.vivitasol.carcasamvvm.navigation.Route
import com.vivitasol.carcasamvvm.viewmodels.CartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserMenuShellView(navController: NavController) {
    val innerNavController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val cartViewModel: CartViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bienvenido") },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            PrefsRepo.setEmail(context, null)
                            navController.navigate(Route.Login.route) {
                                popUpTo(Route.UserMenuShell.route) { inclusive = true }
                            }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = currentRoute == "user_home",
                    onClick = { innerNavController.navigate("user_home") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") },
                    label = { Text("Carrito") },
                    selected = currentRoute == "user_cart",
                    onClick = { innerNavController.navigate("user_cart") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = currentRoute == "user_profile",
                    onClick = { innerNavController.navigate("user_profile") }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = innerNavController,
            startDestination = "user_home",
            modifier = Modifier.padding(padding)
        ) {
            composable("user_home") { UserHomeView(cartViewModel) }
            composable("user_cart") { UserCartView(cartViewModel) }
            composable("user_profile") { UserProfileView() }
        }
    }
}
