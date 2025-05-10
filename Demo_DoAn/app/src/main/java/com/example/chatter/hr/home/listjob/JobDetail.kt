package com.example.chatter.hr.home.listjob

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.chatter.model.Job

@Composable
fun JobDetail(job: Job, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Đóng") }
        },
        title = { Text(text = job.title.toString(), fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("🧾 Tiêu đề: ${job.title}")
                Text("🏢 Công ty: ${job.company}")
                Text("📍 Địa điểm: ${job.address}")
                Text("💰 Mức lương: ${job.salary}")
                Text("📚 Trình độ học vấn: ${job.education}")
                Text("💼 Kinh nghiệm: ${job.experience}")
                Text("👤 Giới tính: ${job.gender}")
                Text("🎂 Độ tuổi yêu cầu: ${job.age}")
                Text("⏱️ Loại hình công việc: ${job.jobType}")
                Text("📡 Hình thức làm việc: ${job.workingForm}")
            }
        }
    )
}


