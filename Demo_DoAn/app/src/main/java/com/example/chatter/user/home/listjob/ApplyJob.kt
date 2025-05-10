package com.example.chatter.user.home.listjob

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatter.model.Job
import com.example.chatter.model.User
import com.example.chatter.model.UserCV
import com.example.chatter.user.home.profile.DropdownSelector
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID


@Composable
fun ApplyJob(
    userProfile: User,
    jobInfo: Job,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    var name by remember { mutableStateOf(userProfile.name ?: "") }
    var gender by remember { mutableStateOf(userProfile.gender ?: "") }
    var education by remember { mutableStateOf(userProfile.education ?: "") }
    var phone by remember { mutableStateOf(userProfile.numberPhone ?: "") }
    var experience by remember { mutableStateOf(userProfile.experience ?: "") }
    var skills by remember { mutableStateOf("") }
    var cvUri by remember { mutableStateOf<Uri?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                // Tạo object lưu
                val userCV = UserCV(
                    id = UUID.randomUUID().toString(),
                    userId = userProfile.id,
                    name = name,
                    gender = gender,
                    education = education,
                    phone = phone,
                    experience = experience,
                    skills = skills.split(",").map { it.trim() },
                    cvUrl = cvUri?.toString() ?: "",
                    jobId = jobInfo.id ?: "",
                    jobTitle = jobInfo.title ?: "",
                    status = 1,
                    createdAt = System.currentTimeMillis()
                )

                val databaseRef = FirebaseDatabase.getInstance().getReference("usercv")
                val key = databaseRef.push().key
                if (key != null) {
                    databaseRef.child(key).setValue(userCV)
                }

                onSubmit() // Thực hiện callback
            }) {
                Text("Gửi đơn")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        title = { Text("Ứng tuyển công việc") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Tải ảnh CV (tuỳ chọn):")
                Button(onClick = {
                }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text("Chọn CV")
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên") })
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Số điện thoại") })

                DropdownSelector("Giới tính", listOf("Nam", "Nữ", "Khác"), gender) { gender = it }
                DropdownSelector(
                    "Trình độ học vấn",
                    listOf("Cử nhân", "Kỹ sư", "Thạc sĩ", "Tiến sĩ"),
                    education
                ) { education = it }
                DropdownSelector(
                    "Kinh nghiệm",
                    listOf("Tôi chưa có kinh nghiệm", "Dưới 1 năm", "1-3 năm", "Trên 3 năm"),
                    experience
                ) { experience = it }

                OutlinedTextField(
                    value = skills,
                    onValueChange = { skills = it },
                    label = { Text("Kỹ năng") },
                    placeholder = { Text("VD: Kotlin, Firebase") }
                )
            }
        }
    )
}



