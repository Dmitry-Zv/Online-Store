package com.vc.onlinestore.presentation.shopping.profilescreen.billingscreen.orderscreen

import com.vc.onlinestore.data.firebase.dto.order.Order

data class OrderState(
    val order: Order? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)