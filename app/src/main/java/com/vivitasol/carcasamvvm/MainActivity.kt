package com.vivitasol.carcasamvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vivitasol.carcasamvvm.navigation.Route
import com.vivitasol.carcasamvvm.ui.theme.AppTheme
import com.vivitasol.carcasamvvm.views.LoginView
import com.vivitasol.carcasamvvm.views.MenuShellView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Route.Login.route
                ) {
                    composable(Route.Login.route) {
                        LoginView(navController = navController)
                    }
                    composable(Route.MenuShell.route) {
                        MenuShellView(navController = navController)
                    }
                }
            }
        }
    }
}
