package com.example.chatter.user.home.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainProfile(navController: NavController) {
    val viewModel: ProfileViewModel = viewModel()

    if (viewModel.editing.value) {
        ProfileEditForm(viewModel)
    } else {
        ProfileScreen(
            profile = viewModel.userProfile.value,
            onEditClick = { viewModel.isEditing() },
            logOutClick = { viewModel.logout(navController) }
        )
    }
}
