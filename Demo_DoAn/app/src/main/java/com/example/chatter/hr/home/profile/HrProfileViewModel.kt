package com.example.chatter.hr.home.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.chatter.model.Company
import com.example.chatter.model.User
import com.google.firebase.auth.FirebaseAuth

class HrProfileViewModel : ViewModel() {
    var hrProfile = mutableStateOf(Company())
    var isEditing = mutableStateOf(false)


    fun updateProfile(newProfile: Company) {
        hrProfile.value = newProfile
    }

    fun editProfile() {
        isEditing.value = !isEditing.value
    }

    fun logout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("login_hr") {
            popUpTo("homeHr") { inclusive = true }
        }
    }
}