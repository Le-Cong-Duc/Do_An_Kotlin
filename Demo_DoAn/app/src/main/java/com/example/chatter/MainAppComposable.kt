package com.example.chatter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatter.feature.auth.signin.SignInScreen
import com.example.chatter.feature.auth.signup.SignUpScreen
import com.example.chatter.feature.chat.ChatScreen
import com.example.chatter.feature.chat.HomeChatScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainApp() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val navController = rememberNavController()

        //Lấy từ fireAuth user hiện tại
        val currentUser = FirebaseAuth.getInstance().currentUser
        // Nếu có người dùng thì vào home ngược laij vào đăng nhaapj
        val checkLogin = if (currentUser != null) "home" else "login"

        // Navhost : định nghia tất cả route
        NavHost(
            navController = navController,
            startDestination = checkLogin
        ) {
            composable("login") {
                SignInScreen(navController)
            }
            composable("signup") {
                SignUpScreen(navController)
            }
            composable("home") {
                HomeChatScreen(navController)
            }
            // điều hướng có thanh số
            composable("chat/{userId}&{userName}", arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                },
                navArgument("userName") {
                    type = NavType.StringType
                }
            )) {
                // lấy giá trị id và năm từ route
                val userId = it.arguments?.getString("userId") ?: ""
                val userName = it.arguments?.getString("userName") ?: ""
                ChatScreen(navController, userId, userName)
            }
        }

    }
}