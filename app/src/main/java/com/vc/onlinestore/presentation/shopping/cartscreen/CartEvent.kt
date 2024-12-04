package com.vc.onlinestore.presentation.shopping.cartscreen

import com.vc.onlinestore.domain.model.CartProduct

sealed class CartEvent {
    data class IncreaseQuantity(val cartProduct: CartProduct) : CartEvent()
    data class DecreaseQuantity(val cartProduct: CartProduct) : CartEvent()
    data class DeleteCartProduct(val cartProduct: CartProduct) : CartEvent()
}