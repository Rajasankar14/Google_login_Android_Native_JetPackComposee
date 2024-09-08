package com.raja.jetpackcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.raja.jetpackcompose.GoogleLoginScreen
import com.raja.jetpackcompose.compose.ProfileScreenDesign
import com.raja.jetpackcompose.compose.SplashScreenDesign

@Composable
fun NavigationScreens(navHostController: NavHostController) {
    NavHost(navHostController, "SplashScreen"){
        composable("SplashScreen"){
            SplashScreenDesign(navHostController)
        }

        composable("LoginScreen"){
            GoogleLoginScreen(navHostController)
        }
        composable("ProfileScreen"){
            ProfileScreenDesign(false,navHostController)
        }

    }
}