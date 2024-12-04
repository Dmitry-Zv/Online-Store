package com.vc.onlinestore.presentation.shopping.profilescreen

import com.vc.onlinestore.data.firebase.dto.User

data class UserState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)
