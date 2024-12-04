package com.vc.onlinestore.utils

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation {
    if (email.isBlank()) {
        return RegisterValidation.Failed(message = "Email cannot be empty")
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return RegisterValidation.Failed(message = "Wrong email format")
    }

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if (password.isBlank()) {
        return RegisterValidation.Failed(message = "Password cannot be empty")
    }
    if (password.length < 8) {
        return RegisterValidation.Failed(message = "Password should contains at least 8 char")
    }

    return RegisterValidation.Success
}