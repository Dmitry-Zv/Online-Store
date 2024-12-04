package com.vc.onlinestore.presentation.common

data class ResetPasswordState(
    val email: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
