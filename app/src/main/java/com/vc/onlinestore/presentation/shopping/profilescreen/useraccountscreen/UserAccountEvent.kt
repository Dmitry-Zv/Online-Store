package com.vc.onlinestore.presentation.shopping.profilescreen.useraccountscreen

import android.net.Uri
import com.vc.onlinestore.data.firebase.dto.User

sealed class UserAccountEvent {
    data class UpdateUser(val user: User, val imageUri: Uri?) : UserAccountEvent()
    data class ResetPassword(val email: String) : UserAccountEvent()
}