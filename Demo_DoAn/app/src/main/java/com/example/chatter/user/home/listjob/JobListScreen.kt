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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatter.model.Job

@Composable
fun JobListScreen(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }
    var isFilterExpanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Phù hợp nhất") }


    val jobs = remember {
        listOf(
            Job(
                1,
                "Frontend Developer",
                listOf("JavaScript", "Angular"),
                "3 năm",
                "đ 15 tr",
                "Cẩm lệ, Đà Nẵng",
                "Tech Company"
            ),
            Job(
                2,
                "AI Developer",
                listOf("Python", "cơ bản"),
                "10-1 năm",
                "đ 10-20 tr",
                "Cẩm lệ, Đà Nẵng",
                "AI Solutions"
            ),
            Job(
                3,
                "Java Spring Boot",
                listOf("Java", "Spring Boot", "kỹ sư"),
                "10 năm",
                "đ 100 tr",
                "Cẩm lệ, Đà Nẵng",
                "Enterprise Systems"
            ),
            Job(
                4,
                "Backend Developer",
                listOf("Java", "kỹ sư"),
                "2 năm",
                "đ 10-15 tr",
                "Hải Châu, Đà Nẵng",
                "Software Company"
            ),
            Job(
                5,
                "Frontend Developer",
                listOf("JavaScript", "Angular"),
                "3 năm",
                "đ 15 tr",
                "Cẩm lệ, Đà Nẵng",
                "Web Solutions"
            )
        )
    }

    Column(
        modifier = modifier
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
            modifier = Modifier.align(Alignment.End)
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
                text = { Text("Mới nhất") },
                onClick = {
                    selectedFilter = "Mới nhất"
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
            items(jobs) { job ->
                JobItem(job = job, onApplyClick = {

                })
            }
        }
    }
}