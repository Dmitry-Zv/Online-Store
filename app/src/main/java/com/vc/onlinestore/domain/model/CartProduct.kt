package com.vc.onlinestore.domain.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "cart_product")
@Parcelize
data class CartProduct(
    @PrimaryKey(autoGenerate = true)
    val cartProductId: Long = 0,
    @Embedded
    val product: Product,
    val quantity: Int,
    val selectedColor: Int? = null,
    val selectedSize: String? = null
) : Parcelable {
    constructor() : this(
        0,
        Product(),
        0,
        null,
        null
    )

    companion object {
        fun findQuantitiesForCartProduct(
            cartProducts: List<CartProduct>,
            cartProductToCheck: CartProduct
        ): Int? =
            cartProducts.find { cartProduct ->
                cartProduct.copy(quantity = 0) == cartProductToCheck.copy(quantity = 0)
            }?.quantity
    }
}