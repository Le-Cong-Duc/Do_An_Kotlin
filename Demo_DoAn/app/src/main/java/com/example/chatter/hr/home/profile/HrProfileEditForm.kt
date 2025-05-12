package com.example.chatter.hr.home.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HrProfileEditForm(viewModel: HrProfileViewModel) {
    val profile = viewModel.companyProfile.value

    var companyName by remember { mutableStateOf(profile.companyName ?: "") }
    var phoneNumber by remember { mutableStateOf(profile.phoneNumber ?: "") }
    var address by remember { mutableStateOf(profile.address ?: "") }
    var email by remember { mutableStateOf(profile.email ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Công ty") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("SĐT công ty") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Địa chỉ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val updatedProfile = profile.copy(
                    companyName = companyName,
                    phoneNumber = phoneNumber,
                    address = address,
                    email = email
                )

                viewModel.updateProfile(updatedProfile)

                viewModel.saveProfile()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xác nhận")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.editing },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Hủy")
        }
    }
}