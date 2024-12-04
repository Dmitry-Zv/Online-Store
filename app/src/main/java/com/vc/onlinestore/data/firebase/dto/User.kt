package com.vc.onlinestore.data.firebase.dto

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val imagePath: String = ""
){
    constructor():this("","","","")
}
