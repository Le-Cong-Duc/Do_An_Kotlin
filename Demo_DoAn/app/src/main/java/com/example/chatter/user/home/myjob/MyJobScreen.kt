package com.example.chatter.user.home.myjob

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatter.model.Job
import com.example.chatter.model.UserCV
import com.google.firebase.auth.FirebaseAuth

enum class JobStatus {
    APPLIED,
    INTERVIEW,
    HIRED
}

@Composable
fun MyJobsScreen(modifier: Modifier) {
    val viewModel: MyJobViewModel = viewModel()
    val userCv by viewModel.userCVs.collectAsState()
    val jobs by viewModel.jobs.collectAsState()

    val currentUser = FirebaseAuth.getInstance().currentUser?.uid

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

        if (currentUser != null) {
            JobCardList(
                status = selectedStatus, userCvs = userCv, jobs = jobs, currentUser,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun JobCardList(
    status: JobStatus,
    userCvs: List<UserCV>,
    jobs: Map<String, Job>,
    curentUser: String,
    viewModel: MyJobViewModel
) {
    val filtered = userCvs.filter {
        when (status) {
            JobStatus.APPLIED -> it.status == 1
            JobStatus.INTERVIEW -> it.status == 2
            JobStatus.HIRED -> it.status == 3
        }
    }

    Column {
        filtered.forEach { cv ->
            val job = jobs[cv.jobId]
            JobCard(cv, job, status, curentUser, viewModel)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun StatusChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) Color(0xFFCCF7FF) else Color.LightGray
    val textColor = if (selected) Color.Black else Color.Black

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
fun JobCard(
    cv: UserCV,
    job: Job?,
    status: JobStatus,
    currentUser: String,
    viewModel: MyJobViewModel
) {
    val backgroundColor = Color(0xFFCCF7FF)

    if (currentUser == cv.userId) {
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
                Text(cv.jobTitle, fontWeight = FontWeight.Bold)
                Text(
                    "Lương: ${job?.salary ?: "???"}",
                    color = Color.Blue,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val skillsToShow = if (job?.skills?.isNotEmpty() == true) job.skills else cv.skills
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                skillsToShow.forEach {
                    Text(
                        text = it ?: "",
                        modifier = Modifier
                            .background(Color.LightGray, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Application status badge
            Box(
                modifier = Modifier
                    .background(Color(0xFFE6F7FF), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    "Đã nộp đơn",
                    fontSize = 11.sp,
                    color = Color.Red
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.deleteCV(cv.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Xoá CV", color = Color.White)
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        when (status) {
            JobStatus.APPLIED -> {
                Text(
                    "Ứng tuyển vào: ${cv.jobTitle}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            JobStatus.INTERVIEW -> {
                Column {
                    Text(
                        "Ngày phỏng vấn: dd/MM/yyyy",
                        fontSize = 12.sp,
                        color = Color(0xFFEFB700)
                    )
                    Text(
                        "Địa chỉ: ${job?.address ?: "???"}",
                        fontSize = 12.sp,
                        color = Color.Green
                    )
                }
            }

            JobStatus.HIRED -> {
                Column {
                    Text(
                        "Chúc mừng bạn đã được nhận!",
                        fontSize = 12.sp,
                        color = Color(0xFF006400)
                    )
                    Text("Ngày đi làm: ???", fontSize = 12.sp)
                }
            }
        }
    }
}
