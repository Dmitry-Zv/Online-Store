package com.vc.onlinestore.presentation.loginregister.loginscreen

import com.google.firebase.auth.FirebaseUser

data class LoginState(
    val user: FirebaseUser? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)