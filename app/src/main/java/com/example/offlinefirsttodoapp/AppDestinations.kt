package com.example.offlinefirsttodoapp

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface AppDestinations {
    val route: String
}

object HomeDestination : AppDestinations {
    override val route: String
        get() = "home"
}

object TaskDestination : AppDestinations {
    override val route: String
        get() = "taskDetails/{taskId}"
    val arguments = listOf(navArgument("taskId") { type = NavType.IntType })

    fun toNavigate(id: Int): String {
        return "taskDetails/${id}"
    }
}