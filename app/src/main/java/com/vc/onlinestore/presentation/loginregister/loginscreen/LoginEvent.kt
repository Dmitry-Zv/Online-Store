package com.vc.onlinestore.presentation.loginregister.loginscreen

import com.vc.onlinestore.data.firebase.dto.User

sealed class LoginEvent {
    data class Login(val email: String, val password: String) : LoginEvent()
    data class ResetPassword(val email: String) : LoginEvent()
    data class SignInWithGoogle(val token:String):LoginEvent()
    data class Authorize(val user: User):LoginEvent()
}