package com.uvg.mypokedex.navigation

sealed class AppScreens(val route: String) {
    object Home : AppScreens("home")
    object Detail : AppScreens("detail/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "detail/$pokemonId"
    }
}
