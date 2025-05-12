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
