package com.vc.onlinestore.presentation.loginregister.registerscreen

import com.google.firebase.auth.FirebaseUser

data class RegisterState(
    val firebaseUser: FirebaseUser? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
