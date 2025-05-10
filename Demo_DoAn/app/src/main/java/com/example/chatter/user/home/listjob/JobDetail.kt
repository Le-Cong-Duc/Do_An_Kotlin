package com.example.chatter.user.home.listjob

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chatter.model.Job

@Composable
fun JobDetail(
    job: Job,
    onDismiss: () -> Unit,
    onApplyClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onApplyClick) {
                Text("Ứng tuyển")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng")
            }
        },
        title = {
            Text("Chi tiết công việc", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
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
                Spacer(modifier = Modifier.height(8.dp))
                Text("🛠 Kỹ năng yêu cầu:")
                job.skills?.forEach {
                    Text("- $it")
                }
            }
        }
    )
}
