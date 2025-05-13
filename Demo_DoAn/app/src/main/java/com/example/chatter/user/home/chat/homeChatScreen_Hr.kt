package com.example.chatter.user.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatter.ui.theme.DarkGrey

// Giao diện home
@Composable
fun HomeChatScreen(navController: NavController) {
    // Tạo viewmodel bằng hilt để tự động Inject
    val viewModel = hiltViewModel<HomeChatViewModel>()

    // lấy danh sách người dùng từ ViewModel
    // collectAsState : giúp UI tự động cập nhật khi dữ liệu thay đổi
    val users = viewModel.user.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF3F9FC)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn {
                //Tiêu đề danh sách
                item {
                    Text(
                        text = "Messages",color = Color(0xFF1B4965),
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                //Thanh tìm kiếm
                item {
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text(text = "Search...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .background(Color(0xFFE2ECF1))
                            .clip(RoundedCornerShape(40.dp)),
                        textStyle = TextStyle(color = Color.LightGray),
                        colors = TextFieldDefaults.colors().copy(
                            focusedContainerColor = DarkGrey,
                            unfocusedContainerColor = Color(0xFFB1DCEF),
                            focusedTextColor = Color.Gray,
                            unfocusedTextColor = Color.Gray,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray,
                            focusedIndicatorColor = Color.Gray
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                    )
                }

                // Hiển thị danh sách người dùng
                items(users.value) { user ->
                    Column {
                        Avatar(
                            user.name,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                        ) {
                            navController.navigate("chat/${user.id}&${user.name}")
                        }
                    }
                }
            }
        }

    }
}

// Giao diện avatar
@Composable
fun Avatar(userName: String, modifier: Modifier, onclick: () -> Unit) {
    Box(modifier = Modifier.clickable {
        onclick()
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(12.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B4965)),
            ) {
                Text(
                    text = userName[0].uppercase(),
                    color = Color.White,
                    style = TextStyle(fontSize = 35.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Text(
                text = userName,
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp,
                color = Color(0xFF102542)
            )
        }
    }

}

