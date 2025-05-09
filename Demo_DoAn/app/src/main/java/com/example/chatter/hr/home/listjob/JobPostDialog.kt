package com.example.chatter.hr.home.listjob

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chatter.model.Job
import kotlin.random.Random


@Composable
fun JobPostDialog(
    existingJob: Job? = null,
    onDismiss: () -> Unit,
    onPost: (Job) -> Unit
) {
    var title by remember { mutableStateOf(existingJob?.title ?: "") }
    var skills by remember { mutableStateOf(existingJob?.skills?.joinToString(", ") ?: "") }
    var experience by remember { mutableStateOf(existingJob?.experience ?: "") }
    var salary by remember { mutableStateOf(existingJob?.salary ?: "") }
    var address by remember { mutableStateOf(existingJob?.address ?: "") }

    var gender by remember { mutableStateOf(existingJob?.gender ?: "Không yêu cầu") }
    var age by remember { mutableStateOf(existingJob?.age ?: "Không yêu cầu") }
    var education by remember { mutableStateOf(existingJob?.education ?: "Đại học") }
    var jobType by remember { mutableStateOf(existingJob?.jobType ?: "Toàn thời gian") }
    var workingForm by remember { mutableStateOf(existingJob?.workingForm ?: "Tại công ty") }

    var step by remember { mutableStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            if (step == 1) {
                TextButton(onClick = { step = 2 }) {
                    Text("Tiếp tục")
                }
            } else {
                TextButton(onClick = {
                    onPost(
                        Job(
                            id = (existingJob?.id ?: Random.nextInt()).toString(),
                            title = title,
                            skills = skills.split(",").map { it.trim() },
                            experience = experience,
                            salary = salary,
                            address = address,
                            company = existingJob?.company ?: "D&D Company",
                            gender = gender,
                            age = age,
                            education = education,
                            jobType = jobType,
                            workingForm = workingForm
                        )
                    )
                }) {
                    Text("Xác nhận")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = {
                if (step == 2) step = 1 else onDismiss()
            }) {
                Text(if (step == 2) "Quay lại" else "Hủy")
            }
        },
        title = {
            Text(
                text = if (existingJob != null) "Chỉnh sửa bài đăng" else "Đăng tin tuyển dụng",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (step == 1) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Tiêu đề công việc") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = skills,
                        onValueChange = { skills = it },
                        label = { Text("Kỹ năng ") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = experience,
                        onValueChange = { experience = it },
                        label = { Text("Kinh nghiệm") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = salary,
                        onValueChange = { salary = it },
                        label = { Text("Mức lương") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Địa điểm") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                } else {
                    DropdownField(
                        "Giới tính",
                        listOf("Không yêu cầu", "Nam", "Nữ"),
                        gender
                    ) { gender = it }
                    DropdownField(
                        "Tuổi",
                        listOf("Không yêu cầu", "Dưới 24", "24-30", "Trên 30"),
                        age
                    ) { age = it }
                    DropdownField(
                        "Trình độ học vấn",
                        listOf("Đại học", "Cử nhân", "Thạc sĩ", "Tiến sĩ"),
                        education
                    ) { education = it }
                    DropdownField(
                        "Loại hình công việc",
                        listOf("Toàn thời gian", "Bán thời gian", "Thực tập"),
                        jobType
                    ) { jobType = it }
                    DropdownField(
                        "Hình thức làm việc",
                        listOf("Tại công ty", "Online", "Linh hoạt"),
                        workingForm
                    ) { workingForm = it }
                }
            }
        }
    )
}


@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = label, modifier = Modifier.padding(bottom = 4.dp))
        Box {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded = true }
                    )
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

