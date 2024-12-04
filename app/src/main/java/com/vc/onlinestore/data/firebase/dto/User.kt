package com.vc.onlinestore.data.firebase.dto

import com.google.firebase.auth.FirebaseUser

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val imagePath: String = ""
) {
    constructor() : this("", "", "", "")

    companion object {
        fun firebaseUserToUser(firebaseUser: FirebaseUser): User =
            User(
                firstName = firebaseUser.displayName?.split(" ")?.getOrNull(0) ?: "",
                lastName = firebaseUser.displayName?.split(" ")?.getOrNull(1) ?: "",
                email = firebaseUser.email ?: "",
                imagePath = firebaseUser.photoUrl?.toString() ?: ""
            )
    }
}
