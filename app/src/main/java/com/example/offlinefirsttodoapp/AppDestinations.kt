package com.example.offlinefirsttodoapp

interface AppDestinations {
    val route: String
}

object Home: AppDestinations {
    override val route: String
        get() = "home"
}

object Details: AppDestinations {
    override val route: String
        get() = "details"
}

object Login: AppDestinations {
    override val route: String
        get() = "login"
}

val appDestinationsScreens = listOf<AppDestinations>(Home, Details, Login)