package com.example.chatter.hr.home.jobApply

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatter.AI.MatchResult
import com.example.chatter.model.Job
import com.example.chatter.model.UserCV


@Composable
fun JobApplyCard(
    cv: MatchResult,
    job: Job?,
    status: JobStatus,
    onReject: () -> Unit,
    onAccept: () -> Unit,
    onPass: () -> Unit,
    onFail: () -> Unit,
    onChat: () -> Unit,
    onPickDate: (String) -> Unit,
    onDateInterview: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    var pickedDate by remember { mutableStateOf("") }

    when (status) {
        JobStatus.APPLIED -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2FAFF)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (job != null) {
                        job.title?.let { Text(it, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("👤 ${cv.userCV.name}", fontSize = 14.sp)
                    Text("📞 SĐT: ${cv.userCV.phone ?: "--"}", fontSize = 14.sp)
                    Text("💼 Kinh nghiệm: ${cv.userCV.experience ?: "--"}", fontSize = 14.sp)
                    Text("🛠 Kỹ năng: ${cv.userCV.skills.joinToString()}", fontSize = 14.sp)
                    Text("⭐ Điểm phù hợp: ${"%.2f".format(cv.score)}", color = Color(0xFF0077B6))

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = onReject) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Từ chối",
                                tint = Color.Red
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onAccept) {
                            Text("Accept")
                        }
                    }
                }
            }
        }

        JobStatus.INTERVIEW -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F7EC)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (job != null) {
                        job.title?.let { Text(it, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("👤 ${cv.userCV.name}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("💼 Kinh nghiệm: ${cv.userCV.experience ?: "--"}", fontSize = 14.sp)
                    Text("🛠 ${cv.userCV.skills.joinToString()}", fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = cv.userCV.dateInterView?.let { "📅 Ngày phỏng vấn: $it" }
                            ?: "📅 Chưa chọn ngày phỏng vấn",
                        color = if (cv.userCV.dateInterView == null) Color.Red else Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { showPicker = true }) {
                        Text("Chọn ngày phỏng vấn")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = onPass) { Text("Pass") }
                        OutlinedButton(onClick = onFail) { Text("Fail") }
                        OutlinedButton(onClick = onChat) {
                            Icon(Icons.Default.Message, contentDescription = "Chat")
                        }
                    }
                }
            }

            if (showPicker) {
                AlertDialog(
                    onDismissRequest = { showPicker = false },
                    title = { Text("Chọn ngày phỏng vân") },
                    text = {
                        OutlinedTextField(
                            value = pickedDate,
                            onValueChange = { pickedDate = it },
                            label = { Text("dd/MM/yyyy") }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            onDateInterview(pickedDate)
                            showPicker = false
                        }) {
                            Text("Submit")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showPicker = false }) {
                            Text("Huỷ")
                        }
                    }
                )
            }
        }

        JobStatus.PASS -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE9F7FF)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (job != null) {
                        job.title?.let { Text(it, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("👤 ${cv.userCV.name}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("🛠 ${cv.userCV.skills.joinToString()}", fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = cv.userCV.date?.let { "📅 Ngày đi làm: $it" } ?: "📅 Chưa chọn ngày đi làm",
                        color = if (cv.userCV.date == null) Color.Red else Color.Black,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { showPicker = true }) {
                        Text("Chọn ngày làm")
                    }
                }
            }

            if (showPicker) {
                AlertDialog(
                    onDismissRequest = { showPicker = false },
                    title = { Text("Chọn ngày đi làm") },
                    text = {
                        OutlinedTextField(
                            value = pickedDate,
                            onValueChange = { pickedDate = it },
                            label = { Text("dd/MM/yyyy") }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            onPickDate(pickedDate)
                            showPicker = false
                        }) {
                            Text("Submit")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showPicker = false }) {
                            Text("Huỷ")
                        }
                    }
                )
            }
        }

    }
}