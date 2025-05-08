package com.example.chatter.user.home.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chatter.model.UserProfile

class ProfileViewModel : ViewModel() {
    var userProfile = mutableStateOf(UserProfile())
    var isEditing = mutableStateOf(false)

    val locationOptions = listOf("Hà Nội", "Đà Nẵng", "TP.HCM")
    val genderOptions = listOf("Nam", "Nữ", "Khác")
    val educationOptions = listOf("Cử nhân", "Thạc sĩ", "Tiến sĩ")
    val experienceOptions = listOf("Tôi chưa có kinh nghiệm", "Dưới 1 năm", "1-3 năm", "Trên 3 năm")

    fun updateProfile(newProfile: UserProfile) {
        userProfile.value = newProfile
    }

    fun toggleEditMode() {
        isEditing.value = !isEditing.value
    }
}
