package com.example.chatter.user.auth.signin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun SignInScreen(navController: NavController) {
    val viewModel: SignInViewModel = hiltViewModel()

    // Tạo 1 state để lưu trạng thái
    val state = viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Chạy mỗi lần state thay đổi
    LaunchedEffect(key1 = state.value) {
        when (state.value) {
            is SignInState.Success -> {
                val currentUser = FirebaseAuth.getInstance().currentUser?.uid
                if (currentUser != null) {
                    val db = FirebaseDatabase.getInstance().getReference("user").child(currentUser)
                    db.get().addOnSuccessListener { dataSnapshot ->
                        if (dataSnapshot.exists()) {
                            val role = dataSnapshot.child("role").getValue(Boolean::class.java)
                            when (role) {
                                true -> {
                                    navController.navigate("homeUser")
                                }

                                false -> {
                                    Toast.makeText(
                                        context,
                                        "Đây là tài khoản Hr\n(không để đăng nhập)",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    password = ""
                                    email = ""
                                }
                                else -> {
                                    Toast.makeText(
                                        context,
                                        "Wrong email or password !!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Không tìm thấy thông tin người dùng",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            else -> {

            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Email")
                }
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Password")
                },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.padding(20.dp))

            if (state.value == SignInState.Loading) {
                CircularProgressIndicator()
            }

            Button(
                onClick = { viewModel.signIn(email, password) },
                modifier = Modifier.fillMaxWidth(),
                // ẩn đi nếu email password k hợp lệ hoặc k phải đang Load
                enabled = email.isNotEmpty() && password.isNotEmpty()
            ) {
                Text(text = "Sign In")
            }

            TextButton(
                onClick = { navController.navigate("signup") }
            ) {
                Text(text = "Don't have an account? Sign Up")
            }

            TextButton(
                onClick = { navController.navigate("login_hr") }
            ) {
                Text(text = "Bạn là HR?")
            }
        }
    }
}

