package com.example.chatter.user.home.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.chatter.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.navigation.NavController


class ProfileViewModel : ViewModel() {
    var userProfile = mutableStateOf(User())
    var isEditing = mutableStateOf(false)

    val addressList = listOf("Hà Nội", "Đà Nẵng", "TP.HCM")
    val genderList = listOf("Nam", "Nữ", "Khác")
    val educationList = listOf("Cử nhân", "Kỹ sư", "Thạc sĩ", "Tiến sĩ")
    val experienceList = listOf("Tôi chưa có kinh nghiệm", "Dưới 1 năm", "1-3 năm", "Trên 3 năm")

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("user/$uid")

        ref.get().addOnSuccessListener { snapshot ->
            snapshot.getValue(User::class.java)?.let {
                userProfile.value = it
            }
        }.addOnFailureListener {
            Log.e("Profile", "Lỗi khi đọc dữ liệu", it)
        }
    }

    fun updateProfile(newProfile: User) {
        userProfile.value = newProfile
    }

    fun saveProfileToFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("user/$uid")

        ref.setValue(userProfile.value).addOnSuccessListener {
            isEditing.value = false
        }.addOnFailureListener {
            Log.e("Profile", "Lỗi khi cập nhật dữ liệu", it)
        }
    }

    fun editProfile() {
        isEditing.value = true
    }

    fun logout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("login") {
            popUpTo("homeUser") { inclusive = true }
        }
    }
}
