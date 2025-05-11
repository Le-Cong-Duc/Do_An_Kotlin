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

    var editing = mutableStateOf(false)

    val addressList = listOf("Hà Nội", "Đà Nẵng", "TP HCM", "Nghệ An", "Quảng Nam", "Quảng Trị")
    val genderList = listOf("Nam", "Nữ")
    val educationList = listOf("Cử nhân", "Kỹ sư", "Thạc sĩ", "Tiến sĩ")
    val experienceList =
        listOf("Chưa có kinh nghiệm", "dưới 1 năm", "dưới 2 năm", "dưới 3 năm", "trên 3 năm")

    init {
        getUserProfile()
    }

    private fun getUserProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("user/$userId")

        dbRef.get().addOnSuccessListener { snapShot ->
            snapShot.getValue(User::class.java)?.let {
                userProfile.value = it
            }
        }.addOnFailureListener {
            Log.e("Profile", "Lỗi khi đọc dữ liệu", it)
        }
    }

    fun updateProfile(profileUpdate: User) {
        userProfile.value = profileUpdate
    }

    fun saveProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("user/$userId")

        dbRef.setValue(userProfile.value)
            .addOnSuccessListener { editing.value = false }
            .addOnFailureListener { Log.e("ProfileViewModel", "Lỗi khi lưu dữ liệu", it) }
    }

    fun isEditing() {
        editing.value = true
    }

    fun logout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("login") {
            popUpTo("homeUser") {
                inclusive = true
            }
        }
    }
}
