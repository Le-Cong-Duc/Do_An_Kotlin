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
            .addOnSuccessListener { task ->
                val user = task.user ?: run {
                    _state.value = SignUpState.Error
                    return@addOnSuccessListener
                }

                val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()

                user.updateProfile(profileUpdates)
                    .addOnSuccessListener {
                        val userInfo = mapOf(
                            "id" to user.uid,
                            "name" to name,
                            "email" to email,
                            "role" to true
                        )

                        FirebaseDatabase.getInstance().getReference("user")
                            .child(user.uid).setValue(userInfo)
                            .addOnSuccessListener {
                                _state.value = SignUpState.Success
                            }.addOnFailureListener {
                                _state.value = SignUpState.Error
                            }

                    }.addOnFailureListener {
                        _state.value = SignUpState.Error
                    }

            }.addOnFailureListener {
                _state.value = SignUpState.Error

            }
    }
}

sealed class SignUpState {
    data object Nothing : SignUpState()
    data object Loading : SignUpState()
    data object Success : SignUpState()
    data object Error : SignUpState()
}
