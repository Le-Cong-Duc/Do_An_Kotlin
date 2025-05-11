package com.example.chatter.hr.home.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun HrMainProfile(navController: NavController) {
    val viewModel: HrProfileViewModel = viewModel()

    val isEditing by viewModel.editing

    if (isEditing) {
        HrProfileEditForm(viewModel)
    } else {
        HrProfileScreen(
            profile = viewModel.companyProfile.value,
            onEditClick = { viewModel.isEditing() },
            onLogoutClick = { viewModel.logout(navController) },
        )
    }
}
