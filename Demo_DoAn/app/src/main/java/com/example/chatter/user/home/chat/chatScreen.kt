package com.example.chatter.user.home.chat

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatter.R
import com.example.chatter.model.Message
import com.example.chatter.ui.theme.Purple
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ChatScreen(userId: String, userName: String) {

    Scaffold(
        containerColor =  Color(0xFFF9F9FB)
    ) {
        val viewModel: ChatViewModel = hiltViewModel()

        val chooseGallery = remember {
            mutableStateOf(false)
        }

        val imageLaucher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                viewModel.sendImageMessage(it, userId)
            }
        }

        Column(modifier = Modifier.padding(it)) {

            LaunchedEffect(key1 = true) {
                viewModel.getListenMessage(userId)
            }

            val message = viewModel.message.collectAsState()

            ChatMessages(
                messages = message.value,
                onSendMessage = { message ->
                    viewModel.sendMesssage(userId, message)
                },
                onImageClicker = {
                    chooseGallery.value = true
                },
                userName = userName
            )
        }

        if (chooseGallery.value) {
            chooseGallery.value = false
            imageLaucher.launch("image/*")
        }


    }
}

@Composable
fun ChatMessages(
    userName: String,
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
    onImageClicker: () -> Unit
) {

    val msg = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            item {
                Avatar(userName = userName,  modifier = Modifier.padding(12.dp)) { }
            }
            items(messages) { message ->
                ChatBox(message = message)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                onImageClicker()
                msg.value = ""
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "gallery")
            }

            TextField(
                value = msg.value,
                onValueChange = { msg.value = it },
                modifier = Modifier.weight(1f)
                    .padding(horizontal = 8.dp)
                    .background(Color(0xFF1B4965)),
                placeholder = { Text(text = "Type a message") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            IconButton(onClick = {
                onSendMessage(msg.value)
                msg.value = ""
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "send")
            }
        }
    }
}

@Composable
fun ChatBox(message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val backgroundColor = if (isCurrentUser) Color(0xFF007BFF) else Color(0xFFEDEDED)
    val textColor = if (isCurrentUser) Color.White else Color.Black
    val boxColor = Purple


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
    ) {
        val alignment = if (isCurrentUser) {
            Alignment.CenterEnd
        } else {
            Alignment.CenterStart
        }

        Row(
            modifier = Modifier
                .padding(8.dp)
                .background(color = boxColor, shape = RoundedCornerShape(8.dp))
                .align(alignment),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isCurrentUser) {
                Image(
                    painter = painterResource(id = R.drawable.account),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Box(
                modifier = Modifier
                    .background(backgroundColor, RoundedCornerShape(16.dp))
                    .padding(12.dp)
                    .widthIn(max = 250.dp)
            ) {
                if (message.imageUrl != null) {
                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = message.message?.trim() ?: "",
                        color = textColor
                    )
                }
            }
        }
    }
}