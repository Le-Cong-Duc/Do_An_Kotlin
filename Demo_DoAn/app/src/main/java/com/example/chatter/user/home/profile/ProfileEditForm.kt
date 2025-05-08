package com.example.chatter.user.home.profile

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
import java.time.LocalDate
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileEditForm(viewModel: ProfileViewModel) {
    val profile = viewModel.userProfile.value
    val dateState = remember { mutableStateOf(profile.birthDate ?: LocalDate.now()) }
    val calendar = Calendar.getInstance().apply {
        set(dateState.value.year, dateState.value.monthValue - 1, dateState.value.dayOfMonth)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = profile.firstName,
            onValueChange = { viewModel.updateProfile(profile.copy(firstName = it)) },
            label = { Text("Tên") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = profile.lastName,
            onValueChange = { viewModel.updateProfile(profile.copy(lastName = it)) },
            label = { Text("Họ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(
            label = "Ngày sinh",
            selectedDate = calendar,
            onDateSelected = { newCalendar ->
                val newDate = LocalDate.of(
                    newCalendar.get(Calendar.YEAR),
                    newCalendar.get(Calendar.MONTH) + 1,
                    newCalendar.get(Calendar.DAY_OF_MONTH)
                )
                dateState.value = newDate
                viewModel.updateProfile(profile.copy(birthDate = newDate))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownSelector("Nơi ở", viewModel.locationOptions, profile.location) {
            viewModel.updateProfile(profile.copy(location = it))
        }

        DropdownSelector("Giới tính", viewModel.genderOptions, profile.gender) {
            viewModel.updateProfile(profile.copy(gender = it))
        }

        DropdownSelector("Trình độ học vấn", viewModel.educationOptions, profile.education) {
            viewModel.updateProfile(profile.copy(education = it))
        }

        DropdownSelector("Kinh nghiệm", viewModel.experienceOptions, profile.experience) {
            viewModel.updateProfile(profile.copy(experience = it))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.toggleEditMode() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xác nhận")
        }
    }
}
