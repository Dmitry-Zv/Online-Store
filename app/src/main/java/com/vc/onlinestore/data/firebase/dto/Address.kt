package com.vc.onlinestore.data.firebase.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    var id: String = "",
    val addressTitle: String,
    val fullName: String,
    val street: String,
    val phone: String,
    val city: String,
    val state: String
) : Parcelable {

    constructor() : this("", "", "", "", "", "", "")
}