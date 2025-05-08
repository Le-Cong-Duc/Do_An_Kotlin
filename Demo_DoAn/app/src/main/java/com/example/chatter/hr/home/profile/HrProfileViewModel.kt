package com.example.chatter.hr.home.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.chatter.model.Company
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HrProfileViewModel : ViewModel() {
    var hrProfile = mutableStateOf(Company())
    var isEditing = mutableStateOf(false)
    val isLoading = mutableStateOf(true)

    val error = mutableStateOf<String?>(null)

    init {
        getProfile()
    }

    private fun getProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            error.value = "User not authenticated"
            isLoading.value = false
            return
        }

        isLoading.value = true
        error.value = null

        val db = FirebaseDatabase.getInstance()
        val company = db.getReference("company").child(currentUser.uid)

        company.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cmp = snapshot.getValue(Company::class.java)
                if (cmp != null) {
                    hrProfile.value = cmp
                }
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                this@HrProfileViewModel.error.value = "Failed to load profile: ${error.message}"
                isLoading.value = false
            }

        })
    }


    fun updateProfile(newProfile: Company) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            error.value = "User not authenticated"
            return
        }

        val updatedProfile = newProfile.copy(uid = currentUser.uid)

        viewModelScope.launch {
            try {
                val database = FirebaseDatabase.getInstance()
                val companyRef = database.getReference("company").child(currentUser.uid)
                companyRef.setValue(updatedProfile).await()
                hrProfile.value = updatedProfile

                // Chỉ đóng form khi dùng nút xác nhận (được quản lý ở HrProfileEditForm)
            } catch (e: Exception) {
                error.value = "Failed to update profile: ${e.message}"
            }
        }
    }

    fun saveProfile() {
        // Đóng form chỉnh sửa sau khi đã lưu thành công
        isEditing.value = false
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