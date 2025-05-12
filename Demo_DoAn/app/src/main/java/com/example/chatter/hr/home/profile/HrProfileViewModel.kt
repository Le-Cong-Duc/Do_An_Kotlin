package com.example.chatter.hr.home.profile

import android.util.Log
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
    var companyProfile = mutableStateOf(Company())
    var editing = mutableStateOf(false)

    init {
        getHrProfile()
    }

    private fun getHrProfile() {
        val cmpId = FirebaseAuth.getInstance().currentUser?.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("company/$cmpId")

        dbRef.get().addOnSuccessListener { snapshot ->
            snapshot.getValue(Company::class.java)?.let {
                companyProfile.value = it
            }
        }.addOnFailureListener {
            Log.e("Profile Hr", "Lỗi khi đọc dữ liệu", it)
        }
    }

    fun updateProfile(profileUpdate: Company) {
        companyProfile.value = profileUpdate
    }

    fun isEditing() {
        editing.value = true
    }

    fun saveProfile() {
        val cmpId = FirebaseAuth.getInstance().currentUser?.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("company/$cmpId")

        dbRef.setValue(companyProfile.value)
            .addOnSuccessListener { editing.value = false }
            .addOnFailureListener { Log.e("HrProfileViewModel", "Lỗi khi lưu dữ liệu", it) }
    }

    fun logout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("login_hr") {
            popUpTo("homeHr") { inclusive = true }
        }
    }
}