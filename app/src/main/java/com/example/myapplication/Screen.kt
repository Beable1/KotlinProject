package com.example.myapplication

sealed class Screen(val route: String) {
    object Home: Screen(route = "home_screen")
    object First: Screen(route = "first_screen")
    object Second: Screen(route = "second_screen")
    object Third: Screen(route = "third_screen")
    object Login: Screen(route = "login_screen")
    object Register: Screen(route = "register_screen")
    object Verification: Screen(route = "verification_screen")
    object Forgot: Screen(route = "forgot_screen")

}