package com.vivitasol.carcasamvvm.views

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.vivitasol.carcasamvvm.LimitedEditionApp
import com.vivitasol.carcasamvvm.R
import com.vivitasol.carcasamvvm.navigation.Route
import com.vivitasol.carcasamvvm.viewmodels.ClienteViewModel
import com.vivitasol.carcasamvvm.viewmodels.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuShellView(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val innerNavController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(250.dp) // Ancho del menú reducido
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logoedicionlimitadapng),
                        contentDescription = "Edición Limitada Logo",
                        modifier = Modifier.height(50.dp)
                    )
                }
                Divider()
                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = currentInnerRoute(innerNavController) == Route.Home.route,
                    onClick = {
                        innerNavController.navigate(Route.Home.route) {
                            popUpTo(Route.Home.route) { inclusive = true }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Detalles") },
                    selected = currentInnerRoute(innerNavController) == Route.Detail.route,
                    onClick = {
                        innerNavController.navigate(Route.Detail.route) {
                            popUpTo(Route.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Crear Producto") },
                    selected = currentInnerRoute(innerNavController) == Route.CreateProduct.route,
                    onClick = {
                        innerNavController.navigate(Route.CreateProduct.route) {
                            popUpTo(Route.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Administrar Clientes") },
                    selected = currentInnerRoute(innerNavController) == Route.ClienteList.route,
                    onClick = {
                        innerNavController.navigate(Route.ClienteList.route) {
                            popUpTo(Route.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Crear Cliente") },
                    selected = currentInnerRoute(innerNavController) == Route.CreateCliente.route,
                    onClick = {
                        innerNavController.navigate(Route.CreateCliente.route) {
                            popUpTo(Route.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Route.Login.route) {
                            popUpTo(Route.MenuShell.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edición Limitada") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = innerNavController,
                startDestination = Route.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Route.Home.route) { HomeView() }
                composable(Route.Detail.route) { DetailView() }
                composable(Route.CreateProduct.route) { CreateProductView(navController = innerNavController) }
                composable(Route.ClienteList.route) { ClienteManagementView(innerPadding) }
                composable(Route.CreateCliente.route) { CreateClienteView() }
            }
        }
    }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun ClienteManagementView(paddingValues: PaddingValues) {
    val activity = LocalContext.current as Activity
    val application = activity.application as LimitedEditionApp
    val vm: ClienteViewModel = viewModel(factory = ViewModelFactory(application, application.repository, application.clientRepository))

    val state by vm.state.collectAsState()
    var showAnimatedMessage by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(state.clienteActualizado, state.clienteEliminado, state.message) {
        when {
            state.clienteActualizado -> {
                message = "Cliente actualizado correctamente"
                showAnimatedMessage = true
                vm.onClienteActualizadoHandled()
            }
            state.clienteEliminado -> {
                message = "Cliente eliminado correctamente"
                showAnimatedMessage = true
                vm.onClienteEliminadoHandled()
            }
            state.message != null -> {
                message = state.message!!
                showAnimatedMessage = true
                vm.onMessageHandled()
            }
        }
    }

    if (showAnimatedMessage) {
        LaunchedEffect(key1 = true) {
            delay(3000) // Duración del mensaje en pantalla AUMENTADA
            showAnimatedMessage = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.selectedCliente == null) {
            ClienteListView(paddingValues = paddingValues, clientes = state.clientes, onClienteClick = vm::onClienteSelected)
        } else {
            ClienteDetailView(
                paddingValues = paddingValues,
                cliente = state.selectedCliente!!,
                vm = vm
            )
        }

        AnimatedVisibility(
            visible = showAnimatedMessage,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 48.dp, vertical = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun currentInnerRoute(navController: NavHostController): String? {
    val entry by navController.currentBackStackEntryAsState()
    return entry?.destination?.route
}
