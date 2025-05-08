package com.example.chatter.hr.home
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class HrCompanyProfile(
    val companyName: String,
    val phoneNumber: String,
    val address: String,
    val email: String
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HrProfileEditForm(
    profile: HrProfile,
    onProfileUpdated: (HrProfile) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val companyNameState = remember { mutableStateOf(profile.companyName) }
    val phoneNumberState = remember { mutableStateOf(profile.phoneNumber) }
    val addressState = remember { mutableStateOf(profile.address) }
    val emailState = remember { mutableStateOf(profile.email) }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = companyNameState.value,
            onValueChange = { companyNameState.value = it },
            label = { Text("Tên Công ty") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phoneNumberState.value,
            onValueChange = { phoneNumberState.value = it },
            label = { Text("SĐT Công ty") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = addressState.value,
            onValueChange = { addressState.value = it },
            label = { Text("Địa chỉ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email liên hệ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onProfileUpdated(
                    HrProfile(
                        companyName = companyNameState.value,
                        phoneNumber = phoneNumberState.value,
                        address = addressState.value,
                        email = emailState.value
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xác nhận")
        }
    }
}
