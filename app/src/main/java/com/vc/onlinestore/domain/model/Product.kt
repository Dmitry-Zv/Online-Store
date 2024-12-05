package com.vc.onlinestore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Long,
    val name: String,
    val category: String,
    val price: Float,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null,
    val size: List<String>? = null,
    val images: List<String>
) : Parcelable{
    constructor():this(0, "", "", 0f, null, null, null, null, emptyList())
}
