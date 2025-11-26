package com.vivitasol.carcasamvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vivitasol.carcasamvvm.navigation.Route
import com.vivitasol.carcasamvvm.ui.theme.AppTheme
import com.vivitasol.carcasamvvm.viewmodels.MainViewModel
import com.vivitasol.carcasamvvm.viewmodels.StartDestination
import com.vivitasol.carcasamvvm.viewmodels.ViewModelFactory
import com.vivitasol.carcasamvvm.views.LoginView
import com.vivitasol.carcasamvvm.views.MenuShellView
import com.vivitasol.carcasamvvm.views.RegisterView
import com.vivitasol.carcasamvvm.views.UserMenuShellView

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels {
        val app = application as LimitedEditionApp
        ViewModelFactory(app, app.repository, app.clientRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            vm.startDestination.value is StartDestination.Loading
        }

        setContent {
            AppTheme {
                val startDestination by vm.startDestination.collectAsState()

                val currentDestination = startDestination
                if (currentDestination is StartDestination.Destination) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = currentDestination.route
                    ) {
                        composable(Route.Login.route) {
                            LoginView(navController = navController)
                        }
                        composable(Route.Register.route) {
                            RegisterView(navController = navController)
                        }
                        composable(Route.MenuShell.route) {
                            MenuShellView(navController = navController)
                        }
                        composable(Route.UserMenuShell.route) {
                            UserMenuShellView(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
