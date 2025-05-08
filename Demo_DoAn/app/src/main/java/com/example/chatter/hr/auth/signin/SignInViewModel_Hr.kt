package com.example.chatter.hr.auth.signin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel_Hr @Inject constructor() : ViewModel() {
    // tạo đối tượng lưu trữ trạng thái , mặc định là Nothing (không làm gì cả)
    private val _state = MutableStateFlow<SignInState>(SignInState.Nothing)

    var state = _state.asStateFlow()

    //đăng nhập bằng mail và password
    fun signIn(email: String, password: String) {
        // đặt trạng thái là load
        _state.value = SignInState.Loading
        // sử dụng phương thức đăng nhập của firebase
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //kiểm tra xem kết quả trả về có null không
                    task.result.user?.let {
                        // nếu đăng nhập thành công trạng thái được cập nhật là success
                        _state.value = SignInState.Success
                        // dùng để thoát khỏi lamda và kết thúc hàm chứa nó
                        return@addOnCompleteListener
                    }
                    _state.value = SignInState.Error
                } else {
                    _state.value = SignInState.Error
                }
            }
    }
}

// quy định trạng thái đăng nhập
sealed class SignInState {
    object Nothing : SignInState()
    object Loading : SignInState()
    object Success : SignInState()
    object Error : SignInState()
}