package com.example.chatter.hr.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.chatter.model.Job_Hr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HrHomeScreen(modifier: Modifier = Modifier.fillMaxSize()) {
    var showDialog by remember { mutableStateOf(false) }
    val jobPosts = remember { mutableStateListOf<Job_Hr>() }
    var jobToEdit by remember { mutableStateOf<Job_Hr?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý tuyển dụng") },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Đăng tin")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyColumn {
                items(jobPosts) { job ->
                    JobItemWithEdit(job = job, onEditClick = {
                        jobToEdit = job
                        showDialog = true
                    })
                }
            }
        }
    }

    if (showDialog) {
        JobPostDialog(
            existingJob = jobToEdit,
            onDismiss = {
                showDialog = false
                jobToEdit = null
            },
            onPost = { newJob ->
                if (jobToEdit != null) {
                    val index = jobPosts.indexOf(jobToEdit)
                    if (index != -1) {
                        jobPosts[index] = newJob
                    }
                } else {
                    jobPosts.add(newJob)
                }
                showDialog = false
                jobToEdit = null
            }
        )
    }

}
