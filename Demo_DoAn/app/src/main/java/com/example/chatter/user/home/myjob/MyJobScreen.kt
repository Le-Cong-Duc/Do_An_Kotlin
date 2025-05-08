package com.example.chatter.user.home.myjob

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class JobStatus {
    APPLIED,
    INTERVIEW,
    HIRED
}


@Composable
fun MyJobsScreen(modifier: Modifier) {
    var selectedStatus by remember { mutableStateOf(JobStatus.APPLIED) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("My jobs", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatusChip("Đã ứng tuyển", selectedStatus == JobStatus.APPLIED) {
                selectedStatus = JobStatus.APPLIED
            }
            StatusChip("Nhận phỏng vấn", selectedStatus == JobStatus.INTERVIEW) {
                selectedStatus = JobStatus.INTERVIEW
            }
            StatusChip("Được nhận", selectedStatus == JobStatus.HIRED) {
                selectedStatus = JobStatus.HIRED
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        JobCard(status = selectedStatus)
    }
}

@Composable
fun StatusChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) Color(0xFFCCF7FF) else Color.LightGray
    val textColor = if (selected) Color.White else Color.Black

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = Modifier
            .defaultMinSize(minWidth = 100.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = textColor,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun JobCard(status: JobStatus) {
    val backgroundColor = Color(0xFFCCF7FF)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Fontend Developer", fontWeight = FontWeight.Bold)
            Text("15 tr", color = Color.Blue, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("JavaScript", "Angular", "cử nhân", "3 năm").forEach {
                Text(
                    text = it,
                    modifier = Modifier
                        .background(Color.LightGray, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (status) {
            JobStatus.APPLIED -> {
                Text("Cẩm lệ , Đà Nẵng", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }

            JobStatus.INTERVIEW -> {
                Column {
                    Text("Ngày phỏng vấn: 05/05/2025", fontSize = 12.sp, color = Color(0xFFEFB700))
                    Text(
                        "Địa chỉ: 26 Trần Hưng Đạo, Ngũ Hành Sơn, Đà Nẵng",
                        fontSize = 12.sp,
                        color = Color.Green
                    )
                }
            }

            JobStatus.HIRED -> {
                Column {
                    Text(
                        "Chúc mừng bạn đã được nhận vào công ty",
                        fontSize = 12.sp,
                        color = Color(0xFF006400)
                    )
                    Text("Ngày đi làm chính thức: 09/05/2025", fontSize = 12.sp)
                }
            }
        }
    }
}




