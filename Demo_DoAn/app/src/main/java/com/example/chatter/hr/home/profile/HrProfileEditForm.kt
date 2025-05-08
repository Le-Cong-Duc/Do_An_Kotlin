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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HrProfileEditForm(viewModel: HrProfileViewModel) {
    val profile = viewModel.hrProfile.value

    Column(modifier = Modifier.padding(16.dp)) {

        profile.companyName?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { viewModel.updateProfile(profile.copy(companyName = it)) },
                label = { Text("Công ty") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        profile.phoneNumber?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { viewModel.updateProfile(profile.copy(phoneNumber = it)) },
                label = { Text("SĐT công ty") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        profile.address?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { viewModel.updateProfile(profile.copy(address = it)) },
                label = { Text("Địa chỉ") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        profile.email?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { viewModel.updateProfile(profile.copy(email = it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.editProfile() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xác nhận")
        }
    }
}
