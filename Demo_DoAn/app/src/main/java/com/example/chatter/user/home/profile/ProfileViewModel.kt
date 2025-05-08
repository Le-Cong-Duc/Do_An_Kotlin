package com.example.chatter.user.home.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.chatter.model.User
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel : ViewModel() {
    var userProfile = mutableStateOf(User())
    var isEditing = mutableStateOf(false)

    val addressList = listOf("Hà Nội", "Đà Nẵng", "TP.HCM")
    val genderList = listOf("Nam", "Nữ", "Khác")
    val educationList = listOf("Cử nhân", "Thạc sĩ", "Tiến sĩ")
    val experienceList = listOf("Tôi chưa có kinh nghiệm", "Dưới 1 năm", "1-3 năm", "Trên 3 năm")

    fun updateProfile(newProfile: User) {
        userProfile.value = newProfile
    }

    fun editProfile() {
        isEditing.value = !isEditing.value
    }

    fun logout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("login") {
            popUpTo("homeUser") { inclusive = true }
        }
    }
}
