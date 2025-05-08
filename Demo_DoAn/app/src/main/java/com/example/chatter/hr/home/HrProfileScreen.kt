package com.example.chatter.hr.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class HrProfile(
    val companyName: String,
    val phoneNumber: String,
    val address: String,
    val email: String
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HrProfileScreen(
    profile: HrProfile,
    onEditClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                        text = profile.companyName,
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = onEditClick) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Chỉnh sửa")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("📞 SĐT: ${profile.phoneNumber}")
                Text("🏢 Địa chỉ: ${profile.address}")
                Text("✉️ Email: ${profile.email}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onLogoutClick, modifier = Modifier.fillMaxWidth()) {
            Text("Đăng xuất")
        }
    }
}


