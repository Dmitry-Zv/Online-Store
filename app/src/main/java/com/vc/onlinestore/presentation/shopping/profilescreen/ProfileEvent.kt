package com.vc.onlinestore.presentation.shopping.profilescreen

sealed class ProfileEvent {
    data object Logout : ProfileEvent()
}