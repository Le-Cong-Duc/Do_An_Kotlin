package com.example.chatter.hr.home.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HrMainProfile(navController: NavController) {
    val viewModel: HrProfileViewModel = viewModel()

    val isEditing by viewModel.isEditing
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.error

    if (isEditing) {
        HrProfileEditForm(viewModel)
    } else {
        HrProfileScreen(
            profile = viewModel.hrProfile.value,
            onEditClick = { viewModel.editProfile() },
            onLogoutClick = { viewModel.logout(navController) },
            isLoading = isLoading,
            errorMessage = errorMessage
        )
    }
}
