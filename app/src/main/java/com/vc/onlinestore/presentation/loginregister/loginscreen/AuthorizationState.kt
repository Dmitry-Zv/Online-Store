package com.vc.onlinestore.presentation.loginregister.loginscreen


data class AuthorizationState(
    val token:String?  = null,
    val errorMessage: String? = null
)
