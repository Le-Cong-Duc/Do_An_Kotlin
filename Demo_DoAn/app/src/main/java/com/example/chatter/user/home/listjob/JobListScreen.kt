package com.example.chatter.user.home.listjob

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatter.model.Job
import com.example.chatter.user.home.profile.ProfileViewModel

@Composable
fun JobListScreen(modifier: Modifier) {
    val viewModel: JobViewModel = viewModel()
    val jobs by viewModel.jobs.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var isFilterExpanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Phù hợp nhất") }

    val profileViewModel: ProfileViewModel = viewModel()
    val userProfile = profileViewModel.userProfile.value

    var selectedJob by remember { mutableStateOf<Job?>(null) }
    var selectedJobDetail by remember { mutableStateOf<Job?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tìm kiếm công việc",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Tìm kiếm...") },
            trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { isFilterExpanded = true },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(selectedFilter)
        }

        DropdownMenu(
            expanded = isFilterExpanded,
            onDismissRequest = { isFilterExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Phù hợp nhất") },
                onClick = {
                    selectedFilter = "Phù hợp nhất"
                    isFilterExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Backend Developer") },
                onClick = {
                    selectedFilter = "Backend Developer"
                    isFilterExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Frontend Developer") },
                onClick = {
                    selectedFilter = "Frontend Developer"
                    isFilterExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Lương cao nhất") },
                onClick = {
                    selectedFilter = "Lương cao nhất"
                    isFilterExpanded = false
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.padding(bottom = 80.dp)
        ) {
            val filteredJobs = when (selectedFilter) {
                "Phù hợp nhất" -> jobs  // Hiển thị tất cả
                "Backend Developer" -> jobs.filter {
                    it.category?.contains(
                        "Backend",
                        ignoreCase = true
                    ) == true
                }

                "Frontend Developer" -> jobs.filter {
                    it.category?.contains(
                        "Frontend",
                        ignoreCase = true
                    ) == true
                }

                "Lương cao nhất" -> jobs.sortedByDescending { it.salary ?: 0 }
                else -> jobs
            }
            items(filteredJobs) { job ->
                JobItem(
                    job = job,
                    onApplyClick = {
                        selectedJob = job
                    },
                    onDetailClick = { selectedJobDetail = job }
                )
            }
        }

        selectedJob?.let {
            ApplyJob(
                userProfile = userProfile,
                jobInfo = it,
                onDismiss = { selectedJob = null },
                onSubmit = {
                    selectedJob = null
                }
            )
        }

        selectedJobDetail?.let {
            JobDetail(
                job = it,
                onDismiss = { selectedJobDetail = null },
                onApplyClick = {
                    selectedJob = it
                    selectedJobDetail = null
                }
            )
        }
    }
}