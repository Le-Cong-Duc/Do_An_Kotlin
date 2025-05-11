package com.example.chatter.user.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.chatter.user.home.chat.HomeChatScreen
import com.example.chatter.user.home.listjob.JobListScreen
import com.example.chatter.user.home.myjob.MyJobsScreen
import com.example.chatter.user.home.profile.MainProfile

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserHomeScreen(navController: NavController) {
    var selectItem by remember { mutableIntStateOf(0) }

    val listItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home),
        BottomNavItem("My Job", Icons.Filled.Work),
        BottomNavItem("Chat", Icons.Filled.ChatBubble),
        BottomNavItem("Profile", Icons.Filled.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                listItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                        label = { Text(text = item.title) },
                        selected = selectItem == index,
                        onClick = { selectItem = index }
                    )

                }
            }
        }
    ) {
        when (selectItem) {
            0 -> JobListScreen(modifier = Modifier.padding(it))
            1 -> MyJobsScreen(modifier = Modifier.padding(it))
            2 -> HomeChatScreen(navController = navController)
            3 -> MainProfile(navController = navController)
        }

    }

}

data class BottomNavItem(
    val title: String,
    val icon: ImageVector
)
