package com.example.chatter.hr.home.jobApply

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chatter.model.Job
import com.example.chatter.AI.MatchResult


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeJobApply(navController: NavController) {

    val viewModel: JobApplyViewModel = viewModel()
    val usercv by viewModel.userCV.collectAsState()
    val job by viewModel.job.collectAsState()
    val matchResults by viewModel.matchResults.collectAsState()

    var selectedStatus by remember { mutableStateOf(JobStatus.APPLIED) }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        Text("Apply Jobs", color = Color(0xFF1B4965),
            style = TextStyle(fontSize = 20.sp), fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatusItem("Ung tuyen", selectedStatus == JobStatus.APPLIED) {
                selectedStatus = JobStatus.APPLIED
            }
            StatusItem("Phong van", selectedStatus == JobStatus.INTERVIEW) {
                selectedStatus = JobStatus.INTERVIEW
            }
            StatusItem("Duoc nhan", selectedStatus == JobStatus.PASS) {
                selectedStatus = JobStatus.PASS
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        JobApplyCardList(
            status = selectedStatus,
            userCvs = matchResults,
            jobs = job,
            viewModel = viewModel,
            navController = navController,
        )

    }
}

@Composable
fun StatusItem(text: String, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) Color(0xFFB1DCEF) else Color.LightGray
    val textColor = if (selected) Color.Black else Color.DarkGray

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
fun JobApplyCardList(
    status: JobStatus,
    userCvs: List<MatchResult>,
    jobs: Map<String, Job>,
    viewModel: JobApplyViewModel,
    navController: NavController
) {

    val filtered = when (status) {
        JobStatus.APPLIED -> userCvs.filter { it.userCV.status == 1 }
        JobStatus.INTERVIEW -> userCvs.filter { it.userCV.status == 2 }
        JobStatus.PASS -> userCvs.filter { it.userCV.status == 3 }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    )
    {
        items(filtered) { cv ->
            val job = jobs[cv.userCV.jobId]
            JobApplyCard(
                cv = cv,
                job = job,
                status = status,
                onReject = {
                    viewModel.updateCvStatus(cv.userCV.id, 0)
                },
                onAccept = {
                    viewModel.updateCvStatus(cv.userCV.id, 2)
                },
                onPass = {
                    viewModel.updateCvStatus(cv.userCV.id, 3)
                },
                onFail = {
                    viewModel.updateCvStatus(cv.userCV.id, 4)
                },
                onChat = {
                    navController.navigate("chat/${cv.userCV.userId}&${cv.userCV.name}")
                },
                onPickDate = { date ->
                    viewModel.updateCvDate(cv.userCV.id, date)
                },
                onDateInterview = { date ->
                    viewModel.updateCvDateInterview(cv.userCV.id, date)
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

    }

}

enum class JobStatus {
    APPLIED,
    INTERVIEW,
    PASS
}

