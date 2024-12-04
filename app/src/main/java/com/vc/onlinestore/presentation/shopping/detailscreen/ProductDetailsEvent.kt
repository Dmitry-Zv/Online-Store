package com.vc.onlinestore.presentation.shopping.detailscreen

import com.vc.onlinestore.domain.model.CartProduct

sealed class ProductDetailsEvent {
    data class AddToCart(val cartProduct: CartProduct) : ProductDetailsEvent()
}