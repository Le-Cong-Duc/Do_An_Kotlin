package com.example.chatter.user.home.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel(), modifier: Modifier) {
    if (viewModel.isEditing.value) {
        ProfileEditForm(viewModel)
    } else {
        ProfileDisplayContent(profile = viewModel.userProfile.value) {
            viewModel.toggleEditMode()
        }
    }
}
