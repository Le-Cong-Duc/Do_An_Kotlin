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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatter.model.Job

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HrHomeScreen(modifier: Modifier = Modifier) {

    val viewModel: HrJobViewModel = viewModel()
    val jobs by viewModel.job.collectAsState()

    var showAddForm by remember { mutableStateOf(false) }
    var showEditForm by remember { mutableStateOf<Job?>(null) }
    var showDetail by remember { mutableStateOf<Job?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    if (showAddForm) {
        JobPostDialog(
            existingJob = showEditForm,
            onDismiss = {
                showAddForm = false
                showEditForm = null
            },
            onPost = { newJob ->
                viewModel.addJob(newJob)
                showAddForm = false
                showEditForm = null
            }
        )
    }

    if (showDetail != null) {
        JobDetail(
            job = showDetail!!,
            onDismiss = { showDetail = null }
        )
    }


    LaunchedEffect(key1 = true) {
        viewModel.getJob()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý tuyển dụng") },
                actions = {
                    IconButton(onClick = {
                        showEditForm = null
                        showAddForm = true
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Đăng tin")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (jobs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Không có tin tuyển dụng")
                }
            } else {

                LazyColumn(modifier = Modifier.padding(bottom = 80.dp)) {
                    items(jobs) { job ->
                        JobItem(
                            job = job,
                            onEditClick = {
                                showEditForm = job
                                showAddForm = true
                            },
                            onDeleteClick = {
                                job.id?.let { viewModel.deleteJob(it) }
                            },
                            onCardClick = {
                                showDetail = job
                            }
                        )
                    }
                }

            }
        }
    }

}