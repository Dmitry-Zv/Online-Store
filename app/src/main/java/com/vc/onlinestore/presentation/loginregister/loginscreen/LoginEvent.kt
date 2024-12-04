package com.vc.onlinestore.presentation.loginregister.loginscreen

sealed class LoginEvent {
    data class Login(val email: String, val password: String) : LoginEvent()
    data class ResetPassword(val email: String) : LoginEvent()
    data class SignInWithGoogle(val token:String):LoginEvent()
}