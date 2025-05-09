package com.example.chatter.hr.home.listjob

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatter.model.Job

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HrHomeScreen(modifier: Modifier = Modifier.fillMaxSize()) {
    val viewModel: HrJobViewModel = viewModel()
    val jobs by viewModel.jobs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var jobToEdit by remember { mutableStateOf<Job?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Show error message in Snackbar if any
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            if (isLoading && jobs.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column {
                    LazyColumn {
                        items(jobs) { job ->
                            JobItemWithEdit(
                                job = job,
                                onEditClick = {
                                    jobToEdit = job
                                    showDialog = true
                                },
                                onDeleteClick = {
                                    viewModel.deleteJob(job.id)
                                }
                            )
                        }
                    }
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
                    viewModel.updateJob(newJob)
                } else {
                    viewModel.addJob(newJob)
                }
                showDialog = false
                jobToEdit = null
            }
        )
    }
}