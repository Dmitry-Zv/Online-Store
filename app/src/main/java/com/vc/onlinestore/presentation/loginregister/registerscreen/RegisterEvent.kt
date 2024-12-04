package com.vc.onlinestore.presentation.loginregister.registerscreen

import com.vc.onlinestore.data.firebase.dto.User

sealed class RegisterEvent {
    data class Register(val user: User, val password: String) : RegisterEvent()
}