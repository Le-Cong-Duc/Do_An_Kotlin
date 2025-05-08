package com.example.chatter.user.auth.signup

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state = _state.asStateFlow()

    fun signUp(name: String, email: String, password: String) {
        _state.value = SignUpState.Loading

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result.user
                    if (user != null) {
                        // Cập nhật displayName
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()

                        user.updateProfile(profileUpdates).addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                // Thêm người dùng vào Realtime Database
                                val userMap = mapOf(
                                    "id" to user.uid,
                                    "name" to name,
                                    "email" to email,
                                    "role" to true
                                )

                                FirebaseDatabase.getInstance().getReference("user")
                                    .child(user.uid)
                                    .setValue(userMap)
                                    .addOnCompleteListener { dbTask ->
                                        _state.value = if (dbTask.isSuccessful) {
                                            SignUpState.Success
                                        } else {
                                            SignUpState.Error
                                        }
                                    }
                            } else {
                                _state.value = SignUpState.Error
                            }
                        }
                    } else {
                        _state.value = SignUpState.Error
                    }
                } else {
                    _state.value = SignUpState.Error
                }
            }
    }
}

sealed class SignUpState {
    object Nothing : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    object Error : SignUpState()
}
