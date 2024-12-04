package com.vc.onlinestore.presentation.common

import com.vc.onlinestore.domain.model.CartProduct

data class CartProductsState(
    val product: List<CartProduct>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)