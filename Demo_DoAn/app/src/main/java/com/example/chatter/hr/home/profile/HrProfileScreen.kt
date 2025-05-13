package com.example.chatter.hr.home.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatter.model.Company

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HrProfileScreen(
    profile: Company,
    onEditClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1)),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = profile.companyName ?: "Chưa cập nhật",
                            style = MaterialTheme.typography.titleLarge
                        )
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Chỉnh sửa"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("📞 SĐT: ${profile.phoneNumber ?: "Chưa cập nhật"}")
                    Text("🏢 Địa chỉ: ${profile.address ?: "Chưa cập nhật"}")
                    Text("✉️ Email: ${profile.email ?: "Chưa cập nhật"}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onLogoutClick, modifier = Modifier.fillMaxWidth()) {
                Text("Đăng xuất")
            }
        }

    }
}