package com.example.chatter.hr.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.sharp.MailOutline
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
import com.example.chatter.hr.chat_hr.HomeChatScreen_Hr

data class HrBottomNavItem(
    val title: String,
    val icon: ImageVector
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HrMainScreen(navController: NavController) {
    val items = listOf(
        HrBottomNavItem("Home", Icons.Filled.Home),
        HrBottomNavItem("Chat", Icons.Sharp.MailOutline),
        HrBottomNavItem("Profile", Icons.Filled.Person)
    )

    var selectedItemIndex by remember { mutableStateOf(0) }
    var isEditingProfile by remember { mutableStateOf(false) }

    var profile by remember {
        mutableStateOf(
            HrProfile(
                companyName = "Công ty D&D",
                phoneNumber = "0123456789",
                address = "123 Đường XYZ, Hà Nội",
                email = "hr@D&D.com"
            )
        )
    }

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
            1 -> HomeChatScreen_Hr(navController = navController)
            2 -> if (isEditingProfile) {
                HrProfileEditForm(
                    profile = profile,
                    onProfileUpdated = { updatedProfile ->
                        profile = updatedProfile
                        isEditingProfile = false
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            } else {
                HrProfileScreen(
                    profile = profile,
                    onEditClick = { isEditingProfile = true },
                    onLogoutClick = {
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}


