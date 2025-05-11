package com.example.chatter.user.home.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileEditForm(viewModel: ProfileViewModel) {
    val profile = viewModel.userProfile.value

    val birth = LocalDate.parse(profile.birthDate)
    val dateState = remember { mutableStateOf(birth ?: LocalDate.now()) }

    val calendar = Calendar.getInstance().apply {
        set(
            dateState.value.year,
            dateState.value.monthValue - 1,
            dateState.value.dayOfMonth
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = profile.name,
            onValueChange = { viewModel.updateProfile(profile.copy(name = it)) },
            label = { Text("Tên") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(
            label = "Ngày sinh",
            selectedDate = calendar,
            onDateSelected = { calendar ->
                val dateUpdate = LocalDate.of(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                dateState.value = dateUpdate

                viewModel.updateProfile(profile.copy(birthDate = dateUpdate.toString()))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        profile.numberPhone?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { viewModel.updateProfile(profile.copy(numberPhone = it)) },
                label = { Text("Số điện thoại") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        profile.address?.let {
            DropdownSelector("Nơi ở", viewModel.addressList, it) {
                viewModel.updateProfile(profile.copy(address = it))
            }
        }

        profile.gender?.let {
            DropdownSelector("Giới tính", viewModel.genderList, it) {
                viewModel.updateProfile(profile.copy(gender = it))
            }
        }

        profile.education?.let {
            DropdownSelector("Trình độ học vấn", viewModel.educationList, it) {
                viewModel.updateProfile(profile.copy(education = it))
            }
        }

        profile.experience?.let {
            DropdownSelector("Kinh nghiệm", viewModel.experienceList, it) {
                viewModel.updateProfile(profile.copy(experience = it))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val updatedUser = profile.copy(
                    name = profile.name,
                    address = profile.address,
                    numberPhone = profile.numberPhone,
                    gender = profile.gender,
                    education = profile.education,
                    experience = profile.experience
                )
                viewModel.updateProfile(updatedUser)
                viewModel.saveProfile()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xác nhận")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}