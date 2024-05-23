package com.example.myapplication
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.Screen.*
import com.google.firebase.auth.FirebaseAuth

var start: String = ""

@Composable
fun SetupNavGraph(navController: NavHostController){
    val auth = FirebaseAuth.getInstance()


    if (auth.currentUser!=null){
        start=Screen.Home.route
    }else {
        start=Screen.Login.route
    }
    NavHost(navController = navController, startDestination = start){

        composable(route=Screen.Login.route){
            LoginScreen(navController)
        }
        composable(route=Screen.Register.route){
            RegisterScreen(navController)
        }
        composable(route=Screen.Verification.route){
            VerificationScreen(navController)
        }
        composable(route=Screen.Forgot.route){
            ForgotScreen(navController)
        }
        composable(route=Screen.Home.route){
            HomeScreen(navController)
        }
        composable(route=Screen.First.route){
            FirstScreen(navController)
        }
        composable(route=Screen.Second.route){
                backStackEntry ->
            val textFieldValue = backStackEntry.arguments?.getString("textFieldValue")?.toInt() ?: 10
            SecondScreen(navController = navController, textFieldValue = textFieldValue)

        }
        composable(route=Screen.Third.route){
            ThirdScreen(navController)
        }


    }
}