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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun JobListScreen(modifier: Modifier) {
        var searchText by remember { mutableStateOf("") }
        var isFilterExpanded by remember { mutableStateOf(false) }
        var selectedFilter by remember { mutableStateOf("Phù hợp nhất") }


        val jobs = remember {
//            listOf(
//
//            )
            ""
        }

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

            LazyColumn {
//                items(jobs) { job ->
//                    JobItem(job = job, onApplyClick = {
//
//                    })
//                }
            }
        }
    }
