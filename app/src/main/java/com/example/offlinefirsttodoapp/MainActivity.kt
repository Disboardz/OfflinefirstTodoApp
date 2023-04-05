package com.example.offlinefirsttodoapp

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.offlinefirsttodoapp.ui.screens.home.Home
import com.example.offlinefirsttodoapp.ui.screens.home.HomeViewModel
import com.example.offlinefirsttodoapp.ui.screens.login.Login
import com.example.offlinefirsttodoapp.ui.screens.taskDetails.TaskDetails
import com.example.offlinefirsttodoapp.ui.theme.OfflinefirstTodoAppTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import soup.compose.material.motion.animation.*
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
//        Firebase.auth.useEmulator("10.0.2.2", 9099)
        var keepSplashScreenOpen = true
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                keepSplashScreenOpen
            }
        }
        setContent {
            App(
                closeSplashScreen = { keepSplashScreenOpen = false }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App(
    closeSplashScreen: () -> Unit
) {
    OfflinefirstTodoAppTheme {
        val navController = rememberMaterialMotionNavController()
        val coroutineScope = rememberCoroutineScope()
        val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        coroutineScope.launch {
                            val user = Firebase.auth.currentUser
                            if (user != null) {
                                navController.navigate("main") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }
                            }
                                delay(200)
                                closeSplashScreen()
                        }
                    }
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        Surface(color = MaterialTheme.colorScheme.background) {
            MaterialMotionNavHost(
                navController = navController,
                startDestination = "authentication"
            ) {
                loginGraph(navController)
                mainGraph(navController)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation(startDestination = HomeDestination.route, route = "main") {
        composable(route = HomeDestination.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            val viewModel = hiltViewModel<HomeViewModel>()

            Home(
                viewModel = viewModel,
                navigateToTaskDetails = { taskId ->
                    navController.navigate(
                        TaskDestination.toNavigate(
                            taskId
                        )
                    )
                },
                navigateToLogin = {
                    navController.navigate("authentication") {
                        popUpTo("main") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = TaskDestination.route,
            arguments = TaskDestination.arguments,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeDestination.route)
            }
            val viewModel = hiltViewModel<HomeViewModel>(parentEntry)

            TaskDetails(
                navigateBack = navController::popBackStack,
                updateTask = { task, cb -> viewModel.updateTask(task, cb)},
                deleteTask = { task, cb -> viewModel.deleteTask(task, cb)}
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation(startDestination = "login", route = "authentication") {
        composable(route = "login", enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            Login(navigate = navController::navigate)
        }
    }
}
