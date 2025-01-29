package com.prafullkumar.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

sealed interface AuthScreens {

    @Serializable
    data object Login : AuthScreens

    @Serializable
    data object Register : AuthScreens

    @Serializable
    data object OnBoarding : AuthScreens
}

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val authController = rememberNavController()
    NavHost(navController = authController, startDestination = AuthScreens.OnBoarding) {
        composable<AuthScreens.OnBoarding> {
            OnBoardingScreen(navController = authController) {
                onAuthSuccess()
            }
        }
        composable<AuthScreens.Login> {
            LoginScreen()
        }
        composable<AuthScreens.Register> {
            RegisterScreen()
        }
    }
}

@Composable
internal fun OnBoardingScreen(navController: NavHostController, onAuthSuccess: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to CodeForge")
        Spacer(modifier = Modifier.height(16.dp))
        GoogleSignInButton {
            onAuthSuccess()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAuthSuccess) {
            Text(text = "Continue as Guest")
        }
    }
}

@Composable
internal fun LoginScreen() {

}

@Composable
internal fun RegisterScreen() {

}

