package com.vc.onlinestore.helper

fun Float?.getProductPrice(price: Float): Float {
    if (this == null) {
        return price
    }
    val remainPercentage = 1f - this
    return remainPercentage * price
}