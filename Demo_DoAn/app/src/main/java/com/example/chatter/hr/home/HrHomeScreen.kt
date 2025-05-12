package com.example.chatter.hr.home


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.sharp.Chat
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.chatter.hr.home.chat_hr.HomeChatScreen_Hr
import com.example.chatter.hr.home.jobApply.HomeJobApply
import com.example.chatter.hr.home.listjob.HrHomeScreen
import com.example.chatter.hr.home.profile.HrMainProfile

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HrMainScreen(navController: NavController) {
    val items = listOf(
        HrBottomNavItem("Home", Icons.Filled.Home),
        HrBottomNavItem("Apply Job", Icons.Filled.Work),
        HrBottomNavItem("Chat", Icons.Sharp.Chat),
        HrBottomNavItem("Profile", Icons.Filled.Person)
    )

    var selectedItemIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItemIndex == index,
                        onClick = { selectedItemIndex = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedItemIndex) {
            0 -> HrHomeScreen(modifier = Modifier.padding(innerPadding))
            1 -> HomeJobApply(navController = navController)
            2 -> HomeChatScreen_Hr(navController = navController)
            3 -> HrMainProfile(navController = navController)
        }
    }
}

data class HrBottomNavItem(
    val title: String,
    val icon: ImageVector
)

