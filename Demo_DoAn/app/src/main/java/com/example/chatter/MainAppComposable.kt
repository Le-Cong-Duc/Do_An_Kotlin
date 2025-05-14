package com.example.chatter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatter.hr.home.chat_hr.HomeChatScreen_Hr
import com.example.chatter.hr.home.HrMainScreen
import com.example.chatter.hr.home.profile.HrMainProfile
import com.example.chatter.user.auth.signin.SignInScreen
import com.example.chatter.user.auth.signup.SignUpScreen
import com.example.chatter.user.home.chat.ChatScreen
import com.example.chatter.user.home.chat.HomeChatScreen
import com.example.chatter.user.home.UserHomeScreen
import com.example.chatter.user.home.profile.MainProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun MainApp() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val navController = rememberNavController()
        var startDestination by remember { mutableStateOf("login") }

        LaunchedEffect(Unit) {
            val currentUser = FirebaseAuth.getInstance().currentUser?.uid
            if (currentUser != null) {
                val db = FirebaseDatabase.getInstance().getReference("user").child(currentUser)
                db.get().addOnSuccessListener { dataSnapshot ->
                    val role = dataSnapshot.child("role").getValue(Boolean::class.java)
                    startDestination = if (role == false) "homeHr" else "homeUser"
                }.addOnFailureListener {
                    startDestination = "login"
                }
            }
        }

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable("login") {
                SignInScreen(navController)
            }
            composable("signup") {
                SignUpScreen(navController)
            }

            composable("chat") {
                HomeChatScreen(navController)
            }
            composable("chat_hr") {
                HomeChatScreen_Hr(navController)
            }

            composable("homeUser") {
                UserHomeScreen(navController)
            }
            composable("homeHr") {
                HrMainScreen(navController)
            }

            composable("profileUser") {
                MainProfile(navController)
            }
            composable("profileHr") {
                HrMainProfile(navController)
            }

            composable("chat/{userId}&{userName}", arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                },
                navArgument("userName") {
                    type = NavType.StringType
                }
            )) {

                val userId = it.arguments?.getString("userId") ?: ""
                val userName = it.arguments?.getString("userName") ?: ""
                ChatScreen(navController, userId, userName)
            }
        }


    }
}