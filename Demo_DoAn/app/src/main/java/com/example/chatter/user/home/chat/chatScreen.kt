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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
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
fun ChatScreen(navController: NavController, userId: String, userName: String) {

    Scaffold(
        containerColor = Color.Black
    ) {
        // Tạo viewmodel thông qua hilt từ ChatViewModel
        val viewModel: ChatViewModel = hiltViewModel()

        // Tạo 1 state chọn ảnh từ thư viện
        val chooseGallery = remember {
            mutableStateOf(false)
        }

        // dùng để xử lí việc chọn ảnh từ thư viên của người dùng
        val imageLaucher = rememberLauncherForActivityResult(
            // ActivityResultContracts.GetContent(): dùng để mở thử viện ảnh
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            // nếu uri không null thì gửi đi
            uri?.let {
                // uri được gửi tới sendMessage
                // it là uri được gửi đi
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

// hiển thị cuộc trò chuyện
@Composable
fun ChatMessages(
    userName: String,
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
    onImageClicker: () -> Unit
) {

    // dùng để ẩn bàn phím ứng dụng đi
    val hideKeyBoardController = LocalSoftwareKeyboardController.current
    // dùng để lưu giữ trạng thái thanh tin nhắn
    val msg = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            //Tên người dùng
            item {
                Avatar(userName = userName, Modifier) { }
            }
            // danh sách tin nhắn
            items(messages) { message ->
                ChatBox(message = message)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                // mở thư viện
                onImageClicker()
                // sau khi nhấn tin nhắn hiện tại sẽ dc xóa
                msg.value = ""
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "gallery")
            }

            TextField(
                // value được lưu trong state msg
                value = msg.value,
                // cập nhật giá trị người dùng thay đổi
                onValueChange = { msg.value = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = "Type a message") },
                // làm cho bàn phím ẩn đi
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    hideKeyBoardController?.hide()
                }),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color.LightGray,
                    unfocusedContainerColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White
                )
            )

            IconButton(onClick = {
                // gửi tin nhắn
                onSendMessage(msg.value)
                msg.value = ""
            }) {
                Icon(imageVector = Icons.Filled.Send, contentDescription = "send")
            }
        }
    }
}

// hiển thị từng tin nhắn
@Composable
fun ChatBox(message: Message) {
    // xem xét tin nhắn gửi đi có phải là người dùng không
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid

    // nếu là người dùng thì tin nhắn có background màu tím ngược lại là xàm
    val boxColor = if (isCurrentUser) {
        Purple
    } else {
        Color.Gray
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
    ) {
        // nếu là người dùng thì tin nhắn bên trái
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
            // nếu không phải người dùng thì sẽ hiển thị icon (avatar)
            if (!isCurrentUser) {
                Image(
                    painter = painterResource(id = R.drawable.account),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            //kiểm tra xem tin nhắn có phải là ảnh không nếu là ảnh thì hiển thị Image còn không thì là Text
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(color = boxColor, shape = RoundedCornerShape(8.dp))
            ) {
                if (message.imageUrl != null) {
                    //AsyncImage : hiển thị hình ảnh dưới dạng url
                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = message.message?.trim() ?: "",
                        color = Color.White
                    )
                }
            }
        }
    }
}