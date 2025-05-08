package com.example.chatter.hr.home.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HrMainProfile(navController: NavController) {
    val viewModel: HrProfileViewModel = viewModel()

    if (viewModel.isEditing.value) {
        HrProfileEditForm(viewModel)
    } else {
        HrProfileScreen(
            profile = viewModel.hrProfile.value,
            { viewModel.editProfile() },
            { viewModel.logout(navController) }
        )
    }
}
